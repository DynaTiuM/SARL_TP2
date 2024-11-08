package ui;

import holarchy.SearchManagerCallback;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Simple class that extends the JFrame class in order to use the JFarme properties
 * for a simple User Interface
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class SearchManagerGUI extends JFrame {
  private SearchManagerPanel panel;

  private JTree tree;

  private DefaultTreeModel treeModel;

  private JScrollPane treeScrollPane;

  @Accessors
  private String rootPath;

  /**
   * This constructor takes in parameters a callback instantiation of class SearchManagerCallback
   * in order to send informations from the UI to the SearchManager and vice versa
   */
  public SearchManagerGUI(final SearchManagerCallback callback) {
    this.setTitle("Search Manager");
    Dimension _dimension = new Dimension(700, 600);
    this.setPreferredSize(_dimension);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    SearchManagerPanel _searchManagerPanel = new SearchManagerPanel(callback, this);
    this.panel = _searchManagerPanel;
    this.getContentPane().add(this.panel, BorderLayout.NORTH);
    DefaultTreeModel _defaultTreeModel = new DefaultTreeModel(null);
    this.treeModel = _defaultTreeModel;
    JTree _jTree = new JTree(this.treeModel);
    this.tree = _jTree;
    JScrollPane _jScrollPane = new JScrollPane(this.tree);
    this.treeScrollPane = _jScrollPane;
    this.treeScrollPane.setVisible(false);
    this.getContentPane().add(this.treeScrollPane, BorderLayout.CENTER);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.requestFocus();
  }

  /**
   * This method update the results to the UI when the search manager returns a list of Files
   * 
   * @param foundFiles: a ConcurrentLinkedQueue of Files
   */
  public void updateResults(final ConcurrentLinkedQueue<File> foundFiles) {
    String _name = new File(this.rootPath).getName();
    final DefaultMutableTreeNode root = new DefaultMutableTreeNode(_name);
    this.panel.createFileTree(root, foundFiles, this.rootPath);
    JLabel _text = this.panel.getText();
    String _defaultFoundFilesText = this.panel.getDefaultFoundFilesText();
    String _string = Integer.valueOf(foundFiles.size()).toString();
    _text.setText((_defaultFoundFilesText + _string));
    JLabel _text_1 = this.panel.getText();
    _text_1.setVisible(true);
    this.treeScrollPane.setVisible(true);
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
  private static final long serialVersionUID = -3800348563L;

  @Pure
  public String getRootPath() {
    return this.rootPath;
  }

  public void setRootPath(final String rootPath) {
    this.rootPath = rootPath;
  }
}
