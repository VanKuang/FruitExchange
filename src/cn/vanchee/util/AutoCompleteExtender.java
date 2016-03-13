package cn.vanchee.util;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Univasity
 */
public class AutoCompleteExtender {

    public static final int DefaultMaxVisibleRows = 5;
    /**
     * �󶨵��ı����
     */
    private JTextComponent textComponent;
    /**
     * ������ʾ����б�ĵ����˵����
     */
    private JPopupMenu popupMenu;
    /**
     * ����չʾƥ�������б����
     */
    private JList resultList;
    /**
     * Ϊ�б��ṩ����֧�ֵ����
     */
    private JScrollPane scrollPane;
    /**
     * �����ṩ��
     */
    private DataProvider dataProvider;
    /**
     * ���ƥ�������Ƿ����ı�
     */
    private boolean matchDataChanged;
    /**
     * ��¼��ǰ��ƥ����ı�
     */
    private String matchedText;
    /**
     * ԭʼ�ı༭�ı�
     */
    private String originalEditText;
    /**
     * ����ƥ����
     */
    private DataMatchHelper dataMatchHelper;
    /**
     * ȷ����������Ĭ��Ϊ����'�س���'������ѡ�ᱻ����
     */
    private CommitListener commitListener;
    /**
     * Ĭ�ϵ����ݸı������
     */
    private final DataProvider.DataChangeListener DefaultDataChangeListener = new DataProvider.DataChangeListener() {

        public void dataChanged(int action, Object value) {
            notifyDataChanged();
        }
    };
    /**
     * �̳߳�
     */
    private BlockingQueue<Runnable> queue; // ���ڴ�������Ķ���
    private ThreadPoolExecutor executor; // �̳߳ض���
    private boolean matchDataAsync = false; // �Ƿ��첽����ƥ�����
    private int resultListWidth;
    private boolean widthWithScrollBar;
    private boolean autoSizeToFit;

    /**
     * ָ���󶨵Ķ��������ṩ����ƥ����������һ������
     *
     * @param textComponent   ����Ϊnull
     * @param dataProvider    ����Ϊnull
     * @param dataMatchHelper ���Ϊnull����ʹ��Ĭ�ϵ�ƥ����
     */
    public AutoCompleteExtender(JTextComponent textComponent, DataProvider dataProvider, DataMatchHelper dataMatchHelper) {
        if (textComponent == null) {
            /**
             * ȷ���󶨵Ĳ����Ϊnull
             */
            throw new IllegalArgumentException("textComponent����Ϊnull!");
        }
        if (dataProvider == null) {
            /**
             * ȷ�������ṩ����Ϊnull
             */
            throw new IllegalArgumentException("dataProvider����Ϊnull!");
        }
        this.textComponent = textComponent;
        this.dataProvider = dataProvider;
        this.dataProvider.setDataChangeListener(DefaultDataChangeListener);
        if (dataMatchHelper == null) {
            this.dataMatchHelper = new DefaultDataMatchHelper();
        } else {
            this.dataMatchHelper = dataMatchHelper;
        }
        /**
         * ��ʼ������
         */
        resetAll();
    }

    /**
     * ָ���󶨵Ķ���ƥ�����ݺ�ƥ����������һ������
     *
     * @param textComponent   ����Ϊnull
     * @param data            ��ʼ��ƥ������
     * @param dataMatchHelper ���Ϊnull����ʹ��Ĭ�ϵ�ƥ����
     */
    public AutoCompleteExtender(JTextComponent textComponent, Object[] data, DataMatchHelper dataMatchHelper) {
        if (textComponent == null) {
            /**
             * ȷ���󶨵Ĳ����Ϊnull
             */
            throw new IllegalArgumentException("textComponent����Ϊnull!");
        }
        this.textComponent = textComponent;
        this.dataProvider = new DefaultDataProvider();
        if (data != null) {
            for (Object value : data) {
                this.dataProvider.appendData(value);
            }
        }
        this.dataProvider.setDataChangeListener(DefaultDataChangeListener);
        if (dataMatchHelper == null) {
            this.dataMatchHelper = new DefaultDataMatchHelper();
        } else {
            this.dataMatchHelper = dataMatchHelper;
        }
        /**
         * ��ʼ������
         */
        resetAll();
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    /**
     * ����ΪĬ�����ã�ԭ�����ݽ������
     */
    public synchronized void resetAll() {
        initTextComponent();
        initResultList();
        initValues();
        setFocusOnTextComponent();
        updateUI();
    }

    /**
     * ˢ�µ�ǰUI
     */
    public synchronized void updateUI() {
        popupMenu.pack();
        popupMenu.updateUI();
    }

    /**
     * ���ƥ����
     */
    public synchronized void clearMatchResult() {
        collapse();
        if (queue != null) {
            queue.clear();
        }
        ((DefaultListModel) resultList.getModel()).removeAllElements();
    }

    /**
     * ���ƥ�����ݸı���
     */
    private void notifyDataChanged() {
        matchDataChanged = true;
    }

    public void setCommitListener(CommitListener commitListener) {
        this.commitListener = commitListener;
    }

    /**
     * ��ȡ��ǰ��ƥ����ı�
     *
     * @return
     */
    public synchronized String getMatchText() {
        return matchedText;
    }

    /**
     * ��ȡ��ǰƥ����
     *
     * @return
     */
    public synchronized Object[] getMatchResult() {
        return ((DefaultListModel) resultList.getModel()).toArray();
    }

    /**
     * ��ȡ��ǰѡ�е�ֵ
     *
     * @return
     */
    public synchronized Object getSelectedValue() {
        return resultList.getSelectedValue();
    }

    /**
     * ȷ��ָ�����ı�Ϊ����ѡ��
     *
     * @param text
     */
    public synchronized void commitText(String text) {
        originalEditText = text;
        textComponent.setText(text);
        if (commitListener != null) {
            commitListener.commit(text);
        }
    }

    /**
     * ��ȡ��ǰѡ���������ֵ
     *
     * @return
     */
    public synchronized int getSelectedIndex() {
        return resultList.getSelectedIndex();
    }

    /**
     * ѡ��ָ��������ֵ
     *
     * @param index
     */
    public synchronized void setSelectedIndex(int index) {
        if (index < 0 || index >= getResultCount()) {
            return;
        }
        resultList.setSelectedIndex(index);
        // ʹѡ����ڿ��ӷ�Χ��
        resultList.ensureIndexIsVisible(index);
    }

    /**
     * �򿪽���б�(���δ��ƥ�䣬���Զ�ִ��ƥ�䴦���������Ч����򲻻ᱻչ��)(�����ת�Ƶ��б�)
     *
     * @return
     */
    public synchronized boolean expand() {
        if (!hasMatched()) {
            if (doMatch()) {
                // չ���б�
                updateExpandListUI();
                popupMenu.show(textComponent, 0, textComponent.getHeight());
            }
        } else if (getResultCount() > 0) {
            popupMenu.setVisible(true);
        }
        return popupMenu.isVisible();
    }

    /**
     * �رս���б�(���ݲ��ᱻ��գ��ٴδ�ʱֱ��������ʾ)
     */
    public synchronized void collapse() {
        removeSelectionInterval();
        popupMenu.setVisible(false);
    }

    /**
     * �жϽ���б��Ƿ񱻴�
     *
     * @return
     */
    public synchronized boolean isExpanded() {
        return popupMenu.isVisible();
    }

    /**
     * ��ȡ��ǰ����б����Ŀ��
     *
     * @return
     */
    public synchronized int getResultCount() {
        return ((DefaultListModel) resultList.getModel()).getSize();
    }

    /**
     * ��ȡһ��������ʾ����(�����Ĳ�����ͨ���϶���������ʾ)
     *
     * @param rows
     */
    public synchronized void setMaxVisibleRows(int rows) {
        resultList.setVisibleRowCount(rows);
    }

    /**
     * �ѽ������õ��ı��༭����
     */
    public synchronized void setFocusOnTextComponent() {
        textComponent.requestFocus();
    }

    /**
     * �ѽ������õ�����б���
     */
    public synchronized void setFocusOnExpandList() {
        resultList.requestFocus();
    }

    /**
     * �жϽ����Ƿ����ı��༭����
     *
     * @return
     */
    public synchronized boolean isFocusOnTextComponent() {
        return textComponent.isFocusOwner();
    }

    /**
     * �жϽ����Ƿ��ڽ���б���
     *
     * @return
     */
    public synchronized boolean isFocusOnExpandList() {
        return resultList.isFocusOwner();
    }

    /**
     * ȡ����ǰ�б��ϵ�ѡ��״̬(ʹselectedIndex==-1)
     */
    public synchronized void removeSelectionInterval() {
        final int selectedIndex = resultList.getSelectedIndex();
        resultList.removeSelectionInterval(selectedIndex, selectedIndex);
    }

    /**
     * �ж��Ƿ��Ѿ�ƥ����ˣ�ƥ��ǰӦ���м�⣬�����ظ�ƥ�������
     *
     * @return
     */
    public synchronized boolean hasMatched() {
        if (matchDataChanged) {
            return false;
        }
        if (matchedText == null || matchedText.length() < 1) {
            return false;
        }
        String text = textComponent.getText();
        if (text == null || !text.equals(matchedText)) {
            return false;
        }
        return true;
    }

    /**
     * ִ��ƥ�����
     *
     * @return
     */
    public synchronized boolean doMatch() {
        // ���ԭ�н��
        clearMatchResult();

        matchedText = textComponent.getText();
        originalEditText = matchedText;
        String keyWord = matchedText;
        if (keyWord != null) {
            keyWord = matchedText;
        }

        if (dataMatchHelper != null) {
            if (!dataMatchHelper.isMatchTextAccept(keyWord)) {
                return false;
            }
        }

        if (matchDataAsync) {
            doMatchAsync(keyWord);
            matchDataChanged = false;
            return true;
        } else {
            doMatchSync(keyWord);
            matchDataChanged = false;
            return getResultCount() > 0;
        }
    }

    /**
     * �����첽ƥ������
     *
     * @param async
     */
    public synchronized void setMatchDataAsync(boolean async) {
        if (this.matchDataAsync != async) {
            this.matchDataAsync = async;
            if (async) {
                queue = new LinkedBlockingQueue<Runnable>();
                // ����һ���������2������֧��10������ ������ʱ20����̳߳�
                executor = new ThreadPoolExecutor(2, 10, 20, TimeUnit.SECONDS, queue);
            } else {
                if (queue != null) {
                    queue.clear();
                }
                if (executor != null) {
                    executor.shutdown();
                }
                queue = null;
                executor = null;
            }
        }
    }

    /**
     * �жϵ�ǰ�Ƿ��첽ƥ��
     *
     * @return
     */
    public synchronized boolean isMatchDataAsync() {
        return this.matchDataAsync;
    }

    /**
     * �ڽ���б�����ʾ����ѡ�������ʾ��
     *
     * @param asNeed �Ƿ������Ҫ��ʾ��true->�ı����ȳ�����ʾ��Χʱ����ʾ��
     */
    public synchronized void showToolTipsWithSelectedValue(boolean asNeed) {
        Object value = resultList.getSelectedValue();
        if (value != null) {
            // ��ʾ��ʾ
            String txt = value.toString();
            if (txt != null) {
                if (asNeed) {
                    // ������Χ����ʾ��ʾ
                    int txtW = SwingUtilities.computeStringWidth(resultList.getFontMetrics(resultList.getFont()), txt);
                    if (txtW >= resultList.getFixedCellWidth()) {
                        resultList.setToolTipText(txt);
                        return;
                    }
                } else {
                    resultList.setToolTipText(txt);
                    return;
                }
            }
        }
        resultList.setToolTipText(null);
    }

    /**
     * �ڽ���б�����ʾָ�����ı���Ϊ��ʾ
     *
     * @param text
     */
    public void showToolTips(String text) {
        if (text != null) {
            resultList.setToolTipText(text);
        } else {
            resultList.setToolTipText(null);
        }
    }

    /**
     * ��ȡһ�����ɼ�����
     *
     * @return
     */
    public synchronized int getMaxVisibleRows() {
        return resultList.getVisibleRowCount();
    }

    /**
     * ��ȡ����б�Ԫ��Ŀ��
     *
     * @return
     */
    public synchronized int getListCellWidth() {
        return resultList.getFixedCellWidth();
    }

    /**
     * ��ȡ����б�Ԫ��ĸ߶�
     *
     * @return
     */
    public synchronized int getListCellHeight() {
        return resultList.getFixedCellHeight();
    }

    public synchronized void setListCellSize(int cellWidth, int cellHeight) {
        resultList.setFixedCellWidth(cellWidth);
        resultList.setFixedCellHeight(cellHeight);
        autoSizeToFit = false;
        updateExpandListUI();
    }

    public synchronized void setListWidth(int width, boolean withScrollBar) {
        this.resultListWidth = width;
        this.widthWithScrollBar = withScrollBar;
        autoSizeToFit = false;
        updateExpandListUI();
    }

    /**
     * ʹ��С�������
     */
    public synchronized void setSizeFitComponent() {
        autoSizeToFit = true;
        updateExpandListUI();
    }

    /**
     * ָ�����Ƿ����ı���Χ��
     *
     * @param p
     * @return
     */
    public synchronized boolean isTextFieldContains(Point p) {
        if (p == null) {
            return false;
        }
        return textComponent.contains(p);
    }

    /**
     * ָ�����Ƿ��ڽ���б�Χ��
     *
     * @param p
     * @return
     */
    public synchronized boolean isExpandListContains(Point p) {
        if (p == null) {
            return false;
        }
        return resultList.contains(p);
    }

    private synchronized void initTextComponent() {
        textComponent.setVisible(true);
        textComponent.setEnabled(true);
        textComponent.setEditable(true);
        // ������ɾ������ӣ�������ظ�....
        textComponent.removeKeyListener(DefaultTextFieldKeyAdapter);
        textComponent.addKeyListener(DefaultTextFieldKeyAdapter);
    }

    private synchronized void initResultList() {
        /**
         * list
         */
        if (resultList != null) {
            resultList.removeAll();
        } else {
            resultList = new JList(new DefaultListModel());
            resultList.addMouseListener(DefaultResultListMouseAdapter);
            resultList.addMouseMotionListener(DefaultResultListMouseMotionAdapter);
        }
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.setVisibleRowCount(DefaultMaxVisibleRows);
        // ������ʾ��
        ToolTipManager.sharedInstance().registerComponent(resultList);

        /**
         * scroll pane
         */
        if (scrollPane == null) {
            scrollPane = new JScrollPane(resultList);
        }
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        /**
         * popup menu
         */
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
        }
        popupMenu.add(scrollPane);
        popupMenu.setVisible(false);
        popupMenu.setFocusable(false);
        popupMenu.setBorder(BorderFactory.createEmptyBorder()); // ȥ���߿�
    }

    private synchronized void initValues() {
        setCommitListener(null);

        matchedText = null;
        matchDataChanged = true;
        this.matchDataAsync = false;
        originalEditText = textComponent.getText();
    }

    /**
     * ���ݸ�����ִֵ��ƥ�����(�ò���Ϊ�첽��)
     *
     * @param content
     * @return
     */
    private synchronized void doMatchAsync(String content) {
        final String matchText = content;

        if (queue != null) {
            queue.clear();
        }

        executor.execute(new Runnable() {

            public void run() {
                /**
                 * ����ƥ��
                 */
                doMatchInner(matchText);
                /**
                 * �����ƥ����رյ�ǰ��ʾ
                 */
                if (getResultCount() > 0) {
                    updateExpandListUI();
                } else {
                    collapse();
                }
            }
        });
    }

    /**
     * ���ݸ�����ִֵ��ƥ�����(�ò���Ϊͬ����)
     *
     * @param content
     * @return
     */
    private synchronized void doMatchSync(String content) {
        /**
         * ����ƥ��
         */
        doMatchInner(content);
    }

    /**
     * ����ƥ��(�ڲ�����)
     *
     * @param matchText
     */
    private void doMatchInner(String matchText) {
        if (dataProvider != null) {
            DefaultListModel listModel = (DefaultListModel) resultList.getModel();
            for (Object value : dataProvider.toArray()) {
                if (dataMatchHelper != null) {
                    if (dataMatchHelper.isDataMatched(matchText, value)) {
                        listModel.addElement(value);
                    }
                } else {
                    // ֱ�����
                    listModel.addElement(value);
                }
            }
        }
    }

    /**
     * ���õ�ǰѡ��Ϊ����ѡ��ֵ
     */
    private void commitTextBySelectedValue() {
        Object value = getSelectedValue();
        if (value != null) {
            commitText(value.toString());
        }
        collapse();
    }

    /**
     * ת�ƽ��㵽�ı��༭�򣬲��رս���б�
     */
    private void changeFocusToTextField() {
        // ȡ��ѡ��
        removeSelectionInterval();
        // ת�ƽ��㵽�ı���
        setFocusOnTextComponent();
        // ����Ϊԭ���༭���ı�ֵ
        textComponent.setText(originalEditText);
    }

    /**
     * ���õ�ǰѡ�����ֵ���ı���
     */
    private void showCurrentSelectedValue() {
        Object value = getSelectedValue();
        if (value != null) {
            textComponent.setText(value.toString());
        }
    }

    /**
     * ˢ�½���б����ʾ(�����ת�Ƶ��б�)
     */
    private synchronized void updateExpandListUI() {
        DefaultListModel listModel = (DefaultListModel) resultList.getModel();
        int dataSize = listModel.getSize();

        int preferredWidth = 0;
        if (autoSizeToFit) {
            /**
             * �Զ�ʹ��С�������
             */
            resultList.setFixedCellWidth(textComponent.getWidth());
            resultList.setFixedCellHeight(textComponent.getHeight());
            preferredWidth = textComponent.getWidth();
            if (dataSize > resultList.getVisibleRowCount()) {
                preferredWidth += scrollPane.getVerticalScrollBar().getPreferredSize().width;
            }
        } else {
            /**
             * ʹ���Զ���Ĵ�С
             */
            preferredWidth = resultListWidth;
            if (dataSize > resultList.getVisibleRowCount()) {
                if (!widthWithScrollBar) {
                    preferredWidth += scrollPane.getVerticalScrollBar().getPreferredSize().width;
                }
            }
        }

        int preferredHeight = Math.min(resultList.getVisibleRowCount(), dataSize) * resultList.getFixedCellHeight() + 3; // ��Ԥ��һЩ�ռ䣬���ֵ���Լ��������Ǻ�׼��

        scrollPane.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        resultList.updateUI();
        popupMenu.pack();
    }

    /**
     * Ĭ���ṩ�Ľ���б�������˶��¼�������
     */
    private MouseMotionAdapter DefaultResultListMouseMotionAdapter = new MouseMotionAdapter() {

        @Override
        public void mouseMoved(MouseEvent e) {
            /**
             * �ò��������:
             * ѡ���������ѡ�����ʾ��ʾ
             */
            Point p = e.getPoint();
            if (isExpandListContains(p)) {
                /**
                 * ������б��������ƶ�ʱ
                 */
                int index = p.y / getListCellHeight();
                // ������
                setSelectedIndex(index);
                // �ı�����ʱ��ʾ��ʾ
                showToolTipsWithSelectedValue(true);
                // ����ع鵽�ı��༭��
                setFocusOnTextComponent();
            }
        }
    };
    /**
     * Ĭ���ṩ�Ľ���б�����갴���¼�������
     */
    private final MouseAdapter DefaultResultListMouseAdapter = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            /**
             * �ò��������:
             * ���ñ༭������Ϊѡ����رս���б�����ص��༭��ͬʱ����commit������
             */
            Point p = e.getPoint();
            if (isExpandListContains(p)) {
                /**
                 * ������б���ʱ
                 */
                int index = p.y / getListCellHeight();
                // ѡ�и���
                setSelectedIndex(index);
                //
                if (getSelectedIndex() == index) {
                    commitTextBySelectedValue();
                }
                // ����ع鵽�ı��༭��
                setFocusOnTextComponent();
            }
        }
    };
    /**
     * Ĭ���ṩ���ı��༭���ϼ��̰����¼�������
     */
    private final KeyAdapter DefaultTextFieldKeyAdapter = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            /**
             * ֻ�Դ��ڵ�ǰ����ʱ������
             */
            if (!e.getComponent().isFocusOwner()) {
                return;
            }

            switch (e.getKeyCode()) {

                case KeyEvent.VK_ENTER:
                    /**
                     * �ò��������:
                     * ���ñ༭������Ϊѡ����رս���б�����ص��༭��ͬʱ����commit������
                     */
                    commitTextBySelectedValue();
                    break;

                case KeyEvent.VK_DOWN:
                    /**
                     * �ò��������:
                     * 1.�������б�δ�򿪣��򿪽���б���ѡ�е�һ����ñ༭������
                     * 2.�����ǰѡ����Ϊ���һ��ý���ص��༭��
                     * 3.��������ѡ����ı�༭������Ϊ��ǰѡ��
                     */
                    if (isExpanded()) {
                        /**
                         * ����б���չ��״̬
                         */
                        final int selectedIndex = getSelectedIndex();
                        if (selectedIndex == getResultCount() - 1) {
                            /**
                             * ����ѡ����Ϊ���һ��
                             */
                            // �����㼯�е��ı���
                            changeFocusToTextField();
                        } else {
                            /**
                             * ����
                             */
                            // ����һ��
                            setSelectedIndex(selectedIndex + 1);
                            showCurrentSelectedValue();
                            setFocusOnTextComponent();
                        }
                    } else {
                        if (expand()) {
                            /**
                             * �ɹ��򿪽���б�
                             */
                            // ѡ�е�һ��
                            setSelectedIndex(0);
                        }
                    }
                    break;

                case KeyEvent.VK_UP:
                    /**
                     * �ò��������:
                     * 1.�������б�δ�򿪣��򿪽���б���ѡ�����һ����ñ༭������
                     * 2.�����ǰѡ����Ϊ��һ��ý���ص��༭��
                     * 3.��������ѡ����ı�༭������Ϊ��ǰѡ��
                     */
                    if (isExpanded()) {
                        /**
                         * ����б���չ��״̬
                         */
                        final int selectedIndex = getSelectedIndex();
                        if (selectedIndex == 0) {
                            /**
                             * ����ѡ����Ϊ��һ��
                             */
                            // �����㼯�е��ı���
                            changeFocusToTextField();
                        } else {
                            /**
                             * ����
                             */
                            if (selectedIndex == -1) {
                                // �Ƶ����һ��
                                setSelectedIndex(getResultCount() - 1);
                            } else {
                                // ����һ��
                                setSelectedIndex(selectedIndex - 1);
                            }
                            showCurrentSelectedValue();
                        }
                    } else {
                        if (expand()) {
                            /**
                             * �ɹ��򿪽���б�
                             */
                            // ѡ�����һ��
                            setSelectedIndex(getResultCount() - 1);
                        }
                    }
                    break;

                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT: // ���ҵĲ�����ͬ
                    /**
                     * �ò��������:
                     * ���ñ༭����Ϊѡ������رս���б�����ص��༭��
                     */
                    if (isExpanded()) {
                        /**
                         * ����б���չ��״̬
                         */
                        if (getSelectedIndex() != -1) {
                            /**
                             * ������ѡ�ѡ����
                             */
                            showCurrentSelectedValue();
                        }
                        collapse();
                    }
                    // ת�ƽ��㵽�ı��༭��
                    changeFocusToTextField();
                    break;
            }
            /**
             * Ϊ��ȷ������ʼ�մ��ڱ༭��
             */
            setFocusOnTextComponent();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (!e.getComponent().isFocusOwner()) {
                return;
            }

            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP
                    || keyCode == KeyEvent.VK_DOWN
                    || keyCode == KeyEvent.VK_LEFT
                    || keyCode == KeyEvent.VK_RIGHT
                    || keyCode == KeyEvent.VK_ENTER) {
                return;
            }
            /**
             * �򿪽���б�
             */
            expand();
            /**
             * Ϊ��ȷ������ʼ�մ��ڱ༭��
             */
            setFocusOnTextComponent();
        }
    };

    /*********************************************************
     * �����һЩ�ӿ�
     */
    public interface CommitListener {

        public void commit(String value);
    }

    /**
     * �����ṩ�ӿ�
     *
     * @author Univasity
     */
    public interface DataProvider {

        public Object getData(int index);

        public void appendData(Object value);

        public void insertData(int index, Object value);

        public void replaceData(int index, Object value);

        public void replaceData(Object oldValue, Object newValue);

        public void removeDataAt(int index);

        public void removeData(Object value);

        public void clear();

        public int getSize();

        public Object[] toArray();

        public void setDataChangeListener(DataChangeListener listener);

        /**
         * ���ݸı�����ӿ�
         */
        public interface DataChangeListener {

            public static final int APPEND = 1;
            public static final int INSERT = 2;
            public static final int REPLACE = 3;
            public static final int REMOVE = 4;
            public static final int CLEAR = 5;

            public void dataChanged(int action, Object value);
        }
    }

    public interface DataMatchHelper {

        /**
         * �ж�ָ��������ƥ���ı��Ƿ�����
         *
         * @param text
         * @return
         */
        public boolean isMatchTextAccept(String text);

        /**
         * �жϸ�����ֵ�Ƿ����ı�ֵƥ��
         *
         * @param matchText
         * @param data
         * @return
         */
        public boolean isDataMatched(String matchText, Object data);
    }

    /*********************************************************
     * Ĭ�ϵ�ʵ��
     */
    private class DefaultDataProvider implements DataProvider {

        private ArrayList data;
        private DataChangeListener listener;

        public DefaultDataProvider() {
            data = new ArrayList();
        }

        public synchronized Object getData(int index) {
            return data.get(index);
        }

        public synchronized void appendData(Object value) {
            if (data.add(value)) {
                if (listener != null) {
                    listener.dataChanged(DataChangeListener.APPEND, value);
                }
            }
        }

        public synchronized void insertData(int index, Object value) {
            data.add(index, value);
            if (listener != null) {
                listener.dataChanged(DataChangeListener.INSERT, value);
            }
        }

        public synchronized void replaceData(int index, Object value) {
            if (data.set(index, value).equals(value)) {
                if (listener != null) {
                    listener.dataChanged(DataChangeListener.REPLACE, value);
                }
            }
        }

        public synchronized void replaceData(Object oldValue, Object newValue) {
            int index = data.indexOf(oldValue);
            if (data.set(index, newValue).equals(newValue)) {
                if (listener != null) {
                    listener.dataChanged(DataChangeListener.REPLACE, newValue);
                }
            }
        }

        public synchronized void removeDataAt(int index) {
            Object value = data.get(index);
            data.remove(index);
            if (listener != null) {
                listener.dataChanged(DataChangeListener.REMOVE, value);
            }
        }

        public synchronized void removeData(Object value) {
            if (data.remove(value)) {
                if (listener != null) {
                    listener.dataChanged(DataChangeListener.REMOVE, value);
                }
            }
        }

        public synchronized void clear() {
            data.clear();
            if (listener != null) {
                listener.dataChanged(DataChangeListener.CLEAR, null);
            }
        }

        public synchronized int getSize() {
            return data.size();
        }

        public synchronized Object[] toArray() {
            return data.toArray();
        }

        public synchronized void setDataChangeListener(DataChangeListener listener) {
            this.listener = listener;
        }
    }

    /**
     * Ĭ�ϵ�����ƥ������
     */
    private class DefaultDataMatchHelper implements DataMatchHelper {

        public boolean isMatchTextAccept(String text) {
            return (text != null && text.length() > 0);
        }

        public boolean isDataMatched(String matchText, Object value) {
            if ((value != null && value.toString().indexOf(matchText) != -1) || "".equals(matchText.trim())) {
                return true;
            }
            return false;
        }
    }
}
