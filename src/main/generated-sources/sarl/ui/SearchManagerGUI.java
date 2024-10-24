package ui;

import holarchy.SearchManagerCallback;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class SearchManagerGUI extends JFrame {
  private SearchManagerPanel panel;

  private JTree tree;

  private DefaultTreeModel treeModel;

  private String rootPath;

  public void setRootPath(final String rootPath) {
    this.rootPath = rootPath;
  }

  public SearchManagerGUI(final SearchManagerCallback callback) {
    this.setTitle("Search Manager GUI");
    Dimension _dimension = new Dimension(800, 600);
    this.setPreferredSize(_dimension);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    SearchManagerPanel _searchManagerPanel = new SearchManagerPanel(callback, this);
    this.panel = _searchManagerPanel;
    this.getContentPane().add(this.panel, BorderLayout.NORTH);
    DefaultTreeModel _defaultTreeModel = new DefaultTreeModel(null);
    this.treeModel = _defaultTreeModel;
    JTree _jTree = new JTree(this.treeModel);
    this.tree = _jTree;
    final JScrollPane treeScrollPane = new JScrollPane(this.tree);
    this.getContentPane().add(treeScrollPane, BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
    this.requestFocus();
  }

  public void updateResults(final ConcurrentLinkedQueue<File> foundFiles) {
    String _name = new File(this.rootPath).getName();
    final DefaultMutableTreeNode root = new DefaultMutableTreeNode(_name);
    final LinkedList<String> fileList = new LinkedList<String>();
    for (final File file : foundFiles) {
      fileList.add(file.getPath());
    }
    this.panel.createFileTree(root, fileList, this.rootPath);
    this.treeModel.setRoot(root);
    this.treeModel.reload();
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SearchManagerGUI other = (SearchManagerGUI) obj;
    if (!Objects.equals(this.rootPath, other.rootPath))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.rootPath);
    return result;
  }

  @SyntheticMember
  private static final long serialVersionUID = -3333941331L;
}
