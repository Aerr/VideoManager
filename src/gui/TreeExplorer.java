/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import misc.Tuple;

/**
 *
 * @author aerr
 */
public class TreeExplorer extends JTree
{

  private final DefaultMutableTreeNode explorerRoot;

  private TreeExplorer()
  {
    javax.swing.tree.DefaultMutableTreeNode treeRoot = new javax.swing.tree.DefaultMutableTreeNode("root");
    explorerRoot = new javax.swing.tree.DefaultMutableTreeNode(new Tuple("Explorer"));
    explorerRoot.add(new javax.swing.tree.DefaultMutableTreeNode(new Tuple("All")));
    treeRoot.add(explorerRoot);
    treeRoot.add(new javax.swing.tree.DefaultMutableTreeNode(new Tuple("Settings")));

    this.setModel(new javax.swing.tree.DefaultTreeModel(treeRoot));
    this.setRootVisible(false);
    this.addMouseListener(new TreeExplorerMouseListener(this));

  }

  public static TreeExplorer getInstance()
  {
    return TreeExplorerHolder.INSTANCE;
  }

  /**
   * @return the explorerRoot
   */
  public DefaultMutableTreeNode getExplorerRoot()
  {
    return explorerRoot;
  }

  private static class TreeExplorerHolder
  {

    private static final TreeExplorer INSTANCE = new TreeExplorer();
  }

  private class TreeExplorerMouseListener extends MouseAdapter
  {

    private final TreeExplorer parent;

    public TreeExplorerMouseListener(TreeExplorer parent)
    {
      this.parent = parent;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      super.mouseClicked(e);
      if (e.getClickCount() == 1)
        Gui.getInstance().populateList(parent.getLastSelectedPathComponent().toString());
    }

  }

}
