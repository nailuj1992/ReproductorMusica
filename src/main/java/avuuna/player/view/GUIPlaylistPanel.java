package avuuna.player.view;

import java.awt.*;

import javax.swing.*;

public class GUIPlaylistPanel extends JPanel {
	private static final long serialVersionUID = -6374452046140600636L;
	
	public static final String listaReproduccion = "Lista de Reproducción";
	public static final String borrarLista = "Borrar lista";
	
	public static final String ACTUAL = "> ";
	
	public JButton clearButton;
	public JScrollPane jScrollPane1;
	public JList<String> songList;
	public DefaultListModel<String> listModel;

	public GUIPlaylistPanel() {
		prepareElementos();
		posicioneElementos();
	}
	
	private void prepareElementos() {
		this.listModel = new DefaultListModel<String>();
		
        songList = new JList<String>();
        songList.setModel(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(songList);
        
        clearButton = new JButton();
        clearButton.setText(borrarLista);
	}
	
	private void posicioneElementos() {
		setBorder(BorderFactory.createTitledBorder(listaReproduccion));
        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(260, 23));
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(clearButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton))
        );

        getAccessibleContext().setAccessibleParent(this);
	}
	
}
