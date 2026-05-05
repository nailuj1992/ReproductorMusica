package avuuna.player.view;

import avuuna.player.utils.Images;
import avuuna.player.utils.Strings;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Main application window. Declares and lays out all UI components.
 * Exposes a clean API for the controller to wire listeners and push state updates.
 */
public class GUIPlayer extends View {
    private static final long serialVersionUID = 422073346876789713L;

    private JMenu fileMenu;
    private JMenuBar menuBar;
    private JMenuItem openItem;

    private JPanel playerPanel;

    private JPanel detailsPanel;
    private JLabel currentSongLabel;
    private JButton playButton;
    private JButton stopButton;
    private JButton prevButton;
    private JButton nextButton;
    private JButton repeatButton;
    private JButton shuffleButton;
    private JProgressBar progressBar;

    private JPanel volumePanel;
    private JSlider volumeSlider;

    private JPanel playlistPanel;

    private JScrollPane listScroll;
    private JList<String> songList;
    private DefaultListModel<String> listModel;

    private JButton addButton;
    private JButton removeButton;
    private JButton clearButton;
    private JButton moveUpButton;
    private JButton moveDownButton;

    public GUIPlayer() {
        super("Music Player - by Avuuna, la Luz del Alba");
        buildComponents();
        buildMenuComponents();
    }

    // -------------------------------------------------------------------------
    // Listener registration
    // -------------------------------------------------------------------------

    public void addOpenMenuListener(ActionListener l) {
        openItem.addActionListener(l);
    }

    public void setOpenMenuAccelerator(KeyStroke ks) {
        openItem.setAccelerator(ks);
    }

    public void addPlayListener(ActionListener l) {
        playButton.addActionListener(l);
    }

    public void addStopListener(ActionListener l) {
        stopButton.addActionListener(l);
    }

    public void addNextListener(ActionListener l) {
        nextButton.addActionListener(l);
    }

    public void addPrevListener(ActionListener l) {
        prevButton.addActionListener(l);
    }

    public void addRepeatListener(ActionListener l) {
        repeatButton.addActionListener(l);
    }

    public void addShuffleListener(ActionListener l) {
        shuffleButton.addActionListener(l);
    }

    public void addVolumeChangeListener(ChangeListener l) {
        volumeSlider.addChangeListener(l);
    }

    public void addSongListMouseListener(MouseListener l) {
        songList.addMouseListener(l);
    }

    public void addAddSongListener(ActionListener l) {
        addButton.addActionListener(l);
    }

    public void addRemoveSongListener(ActionListener l) {
        removeButton.addActionListener(l);
    }

    public void addClearPlaylistListener(ActionListener l) {
        clearButton.addActionListener(l);
    }

    public void addMoveUpListener(ActionListener l) {
        moveUpButton.addActionListener(l);
    }

    public void addMoveDownListener(ActionListener l) {
        moveDownButton.addActionListener(l);
    }

    // -------------------------------------------------------------------------
    // State update methods
    // -------------------------------------------------------------------------

    public void setPlayingState(boolean playing) {
        if (playing) {
            playButton.setToolTipText(Strings.PAUSE);
            playButton.setIcon(Images.getImageIcon(Images.BTN_PAUSE));
        } else {
            playButton.setToolTipText(Strings.PLAY);
            playButton.setIcon(Images.getImageIcon(Images.BTN_PLAY));
        }
    }

    public boolean isShowingPause() {
        return Strings.PAUSE.equals(playButton.getToolTipText());
    }

    public void setSongLabel(String name) {
        currentSongLabel.setText(name != null ? Strings.CURRENT_SONG_LABEL + name : Strings.CURRENT_SONG_LABEL);
    }

    public void setProgressMax(int max) {
        progressBar.setMaximum(max);
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    public void setProgressText(String text) {
        progressBar.setString(text);
    }

    public void addProgressClickListener(MouseListener l) {
        progressBar.addMouseListener(l);
    }

    public void refreshPlaylist(List<String> items) {
        listModel.clear();
        for (String item : items) {
            listModel.addElement(item);
        }
    }

    public boolean isPlaylistEmpty() {
        return listModel.isEmpty();
    }

    public void setRepeatButtonState(Boolean repeatMode) {
        if (repeatMode == null) {
            repeatButton.setToolTipText(Strings.NO_REPEAT);
            repeatButton.setIcon(Images.getImageIcon(Images.BTN_REPEAT_NONE));
        } else if (repeatMode) {
            repeatButton.setToolTipText(Strings.REPEAT_ALL);
            repeatButton.setIcon(Images.getImageIcon(Images.BTN_REPEAT_ALL));
        } else {
            repeatButton.setToolTipText(Strings.REPEAT_ONE);
            repeatButton.setIcon(Images.getImageIcon(Images.BTN_REPEAT_ONE));
        }
    }

    public void setShuffleButtonState(boolean shuffle) {
        if (shuffle) {
            shuffleButton.setToolTipText(Strings.SHUFFLE_ON);
            shuffleButton.setIcon(Images.getImageIcon(Images.BTN_SHUFFLE_ON));
        } else {
            shuffleButton.setToolTipText(Strings.SHUFFLE_OFF);
            shuffleButton.setIcon(Images.getImageIcon(Images.BTN_SHUFFLE_OFF));
        }
    }

    // -------------------------------------------------------------------------
    // Query methods
    // -------------------------------------------------------------------------

    public int getSelectedIndex() {
        return songList.getSelectedIndex();
    }

    public String getSelectedValue() {
        return songList.getSelectedValue();
    }

    public void setSelectedIndex(int index) {
        songList.setSelectedIndex(index);
    }

    public int[] getSelectedIndices() {
        return songList.getSelectedIndices();
    }

    public List<String> getSelectedValues() {
        return songList.getSelectedValuesList();
    }

    public void setSelectedIndices(int[] indices) {
        songList.setSelectedIndices(indices);
    }

    public int getVolumeValue() {
        return volumeSlider.getValue();
    }

    public File[] showOpenDialog() {
        try {
            String script =
                "Add-Type -AssemblyName System.Windows.Forms; " +
                "$d = New-Object System.Windows.Forms.OpenFileDialog; " +
                "$d.Filter = 'MP3 Files (*.mp3)|*.mp3'; " +
                "$d.Multiselect = $true; " +
                "$d.Title = 'Open'; " +
                "if ($d.ShowDialog() -eq 'OK') { $d.FileNames -join [System.Environment]::NewLine }";

            Process process = new ProcessBuilder("powershell", "-NoProfile", "-STA", "-Command", script)
                .start();

            String output = new String(process.getInputStream().readAllBytes()).trim();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return showLegacyOpenDialog();
            }
            if (!output.isEmpty()) {
                return Arrays.stream(output.split("\\r?\\n"))
                    .map(File::new)
                    .toArray(File[]::new);
            }
            return null; // user cancelled
        } catch (Exception ignored) {
            return showLegacyOpenDialog();
        }
    }

    private File[] showLegacyOpenDialog() {
        FileDialog dialog = new FileDialog(this, Strings.OPEN_ITEM, FileDialog.LOAD);
        dialog.setMultipleMode(true);
        dialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".mp3"));
        dialog.setVisible(true);
        File[] files = dialog.getFiles();
        return files.length > 0 ? files : null;
    }

    // -------------------------------------------------------------------------
    // UI construction
    // -------------------------------------------------------------------------

    private void buildMenuComponents() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new JMenu();
        fileMenu.setText(Strings.FILE_MENU);
        menuBar.add(fileMenu);

        openItem = new JMenuItem();
        openItem.setText(Strings.OPEN_ITEM);
        openItem.setIcon(Images.getImageIcon(Images.IMG_OPEN));
        fileMenu.add(openItem);
    }

    private void buildComponents() {

        playerPanel = new JPanel();
        playlistPanel = new JPanel();

        buildPlayerComponents();
        layoutPlayerPanel();

        buildPlaylistComponents();
        layoutPlaylistPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playerPanel, playlistPanel);
        splitPane.setEnabled(false);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void buildPlayerComponents() {
        currentSongLabel = new JLabel();
        currentSongLabel.setText(Strings.CURRENT_SONG_LABEL);
        currentSongLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(0);
        progressBar.setStringPainted(true);
        progressBar.setString(Strings.ZERO_TIME);
        progressBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        prevButton = new JButton();
        prevButton.setToolTipText(Strings.PREV);
        prevButton.setIcon(Images.getImageIcon(Images.BTN_PREV));
        prevButton.setFocusable(false);

        playButton = new JButton();
        playButton.setToolTipText(Strings.PLAY);
        playButton.setIcon(Images.getImageIcon(Images.BTN_PLAY));
        playButton.setFocusable(false);

        stopButton = new JButton();
        stopButton.setToolTipText(Strings.STOP);
        stopButton.setIcon(Images.getImageIcon(Images.BTN_STOP));
        stopButton.setFocusable(false);

        nextButton = new JButton();
        nextButton.setToolTipText(Strings.NEXT);
        nextButton.setIcon(Images.getImageIcon(Images.BTN_NEXT));
        nextButton.setFocusable(false);

        repeatButton = new JButton();
        repeatButton.setToolTipText(Strings.NO_REPEAT);
        repeatButton.setIcon(Images.getImageIcon(Images.BTN_REPEAT_NONE));
        repeatButton.setFocusable(false);

        shuffleButton = new JButton();
        shuffleButton.setToolTipText(Strings.SHUFFLE_OFF);
        shuffleButton.setIcon(Images.getImageIcon(Images.BTN_SHUFFLE_OFF));
        shuffleButton.setFocusable(false);

        volumeSlider = new JSlider();
        volumeSlider.setFocusable(false);
    }

    private void layoutPlayerPanel() {
        playerPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        detailsPanel = new JPanel();
        detailsPanel.setBorder(BorderFactory.createTitledBorder(Strings.PLAYER_PANEL_TITLE));

        GroupLayout dl = new GroupLayout(detailsPanel);
        detailsPanel.setLayout(dl);

        dl.setHorizontalGroup(dl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(dl.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(currentSongLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                .addGroup(dl.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                .addGroup(dl.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(prevButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(playButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(nextButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(repeatButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(shuffleButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)));

        dl.setVerticalGroup(dl.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(currentSongLabel)
                .addGap(5, 5, 5)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(dl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(prevButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(playButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(nextButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(repeatButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(shuffleButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        volumePanel = new JPanel();
        volumePanel.setBorder(BorderFactory.createTitledBorder(Strings.VOLUME_PANEL_TITLE));

        GroupLayout vl = new GroupLayout(volumePanel);
        volumePanel.setLayout(vl);
        vl.setHorizontalGroup(vl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(vl.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(volumeSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap()));
        vl.setVerticalGroup(vl.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(volumeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        GroupLayout layout = new GroupLayout(playerPanel);
        playerPanel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(detailsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(volumePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(detailsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(volumePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
    }

    private void buildPlaylistComponents() {
        listModel = new DefaultListModel<>();

        songList = new JList<>();
        songList.setModel(listModel);
        songList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        listScroll = new JScrollPane();
        listScroll.setViewportView(songList);

        addButton = new JButton();
        addButton.setToolTipText(Strings.ADD_SONG_TOOLTIP);
        addButton.setIcon(Images.getImageIcon(Images.BTN_PLUS));
        addButton.setFocusable(false);

        removeButton = new JButton();
        removeButton.setToolTipText(Strings.REMOVE_SONG_TOOLTIP);
        removeButton.setIcon(Images.getImageIcon(Images.BTN_LESS));
        removeButton.setFocusable(false);

        clearButton = new JButton();
        clearButton.setToolTipText(Strings.CLEAR_PLAYLIST_TOOLTIP);
        clearButton.setIcon(Images.getImageIcon(Images.BTN_CLOSE));
        clearButton.setFocusable(false);

        moveUpButton = new JButton();
        moveUpButton.setToolTipText(Strings.MOVE_UP_TOOLTIP);
        moveUpButton.setIcon(Images.getImageIcon(Images.BTN_MOVE_UP));
        moveUpButton.setFocusable(false);

        moveDownButton = new JButton();
        moveDownButton.setToolTipText(Strings.MOVE_DOWN_TOOLTIP);
        moveDownButton.setIcon(Images.getImageIcon(Images.BTN_MOVE_DOWN));
        moveDownButton.setFocusable(false);
    }

    private void layoutPlaylistPanel() {
        playlistPanel.setBorder(BorderFactory.createTitledBorder(Strings.PLAYLIST_TITLE));
        playlistPanel.setMinimumSize(new Dimension(100, 100));

        GroupLayout layout = new GroupLayout(playlistPanel);
        playlistPanel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(listScroll, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)
                        .addGap(20, 20, 20)
                        .addComponent(moveUpButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moveDownButton)
                        .addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(listScroll, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addButton)
                        .addComponent(removeButton)
                        .addComponent(clearButton)
                        .addComponent(moveUpButton)
                        .addComponent(moveDownButton))
                .addContainerGap());

        playlistPanel.getAccessibleContext().setAccessibleParent(playlistPanel);
    }
}
