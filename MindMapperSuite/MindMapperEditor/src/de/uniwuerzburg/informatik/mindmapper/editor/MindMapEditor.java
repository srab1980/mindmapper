/*
 * MindMapEditor.java
 *
 * Created on 11. Oktober 2008, 19:25
 */

package de.uniwuerzburg.informatik.mindmapper.editor;

import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.UndoRedoManagerCookie;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.openide.awt.UndoRedo;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author  blair
 */
public class MindMapEditor extends CloneableTopComponent implements ExplorerManager.Provider{

    protected UndoRedo.Manager undoRedoManager;
    protected ExplorerManager explorerManager;
    protected Node documentNode;

    protected MultiDataObject dataObject;

    public MindMapEditor() {
        initComponents();
        explorerManager = new ExplorerManager();
        ActionMap map = getActionMap();

        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(explorerManager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(explorerManager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(explorerManager));
        map.put("delete", ExplorerUtils.actionDelete(explorerManager, true));
    }
    /** Creates new form MindMapEditor */
    public MindMapEditor(MultiDataObject dataObject) {
        this();
        this.dataObject = dataObject;
        initFrom(dataObject);
    }
    
    @Override
    protected CloneableTopComponent createClonedObject() {
        MindMapEditor tc = new MindMapEditor(dataObject);
        tc.setDisplayName(documentNode.getName());
        return tc;
    }

    protected void initFrom(MultiDataObject dataObject) {
        documentNode = dataObject.getNodeDelegate();
        setName(documentNode.getLookup().lookup(DocumentCookie.class).getDocument().getName());
        explorerManager.setRootContext(documentNode);

        associateLookup(new ProxyLookup(new Lookup[] {ExplorerUtils.createLookup(explorerManager, getActionMap())}));
        undoRedoManager = documentNode.getLookup().lookup(UndoRedoManagerCookie.class).getUndoRedoManager();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new BeanTreeView();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    @Override
    public UndoRedo getUndoRedo() {
        return undoRedoManager;
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ONLY_OPENED;
    }

    @Override
    protected String preferredID() {
        return "MindMapEditor";
    }

    @Override
    public void writeExternal(ObjectOutput arg0) throws IOException {
        super.writeExternal(arg0);

        arg0.writeObject(dataObject);
    }

    @Override
    public void readExternal(ObjectInput arg0) throws IOException, ClassNotFoundException {
        super.readExternal(arg0);

        dataObject = (MultiDataObject)arg0.readObject();
        initFrom(dataObject);
    }
}
