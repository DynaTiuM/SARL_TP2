package ui;

import holarchy.SearchManagerCallback;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class extends the JPanel class in order to add JPanel properties to the elements that we want to display to our Panel
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class SearchManagerPanel extends JPanel {
  /**
   * The SearchManagerPanel is characterized by:
   * - a textField, that represents a text of the number of files found
   * - a list of extensions, using the newLinkedList collection implemented by SARL
   * - a comboBox, that will store all these extensions in order to be used on the UI
   * - a button "ok" to launc the search
   * - a reference to the SearchManagerCallback in order to send the path and criteria from the UI to the Search Manager
   * - a reference to the parentGUI in order to change the rootPath of the SearchManagerGUI Frame
   * - a label that simply display the path of the folder selected
   */
  private final JTextField textField = new JTextField(20);

  private final LinkedList<String> extensions = CollectionLiterals.<String>newLinkedList(".sarl", ".txt", ".pom", ".xml", ".java", ".md", ".png");

  private final JComboBox<String> comboBox = new JComboBox<String>(((String[])Conversions.unwrapArray(this.extensions, String.class)));

  private final JButton okButton = new JButton("OK");

  private final JButton selectFolderButton = new JButton("Select Folder");

  private final SearchManagerCallback callback;

  private final SearchManagerGUI parentGUI;

  @Accessors
  private final String defaultFoundFilesText = "Number of files found: ";

  @Accessors
  private final JLabel text = new JLabel();

  public SearchManagerPanel(final SearchManagerCallback callback, final SearchManagerGUI parentGUI) {
    this.callback = callback;
    this.parentGUI = parentGUI;
    GridBagLayout _gridBagLayout = new GridBagLayout();
    this.setLayout(_gridBagLayout);
    this.text.setText(this.defaultFoundFilesText);
    this.text.setVisible(false);
    final GridBagConstraints constraints = new GridBagConstraints();
    Insets _insets = new Insets(5, 5, 5, 5);
    constraints.insets = _insets;
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.LINE_END;
    JLabel _jLabel = new JLabel("Path: ");
    this.add(_jLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.LINE_START;
    this.add(this.textField, constraints);
    this.textField.setEditable(false);
    constraints.gridx = 2;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_END;
    JLabel _jLabel_1 = new JLabel("Extension: ");
    this.add(_jLabel_1, constraints);
    constraints.gridx = 3;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_START;
    this.add(this.comboBox, constraints);
    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_END;
    this.add(this.selectFolderButton, constraints);
    constraints.gridx = 3;
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.CENTER;
    this.add(this.okButton, constraints);
    final ActionListener _function = (ActionEvent it) -> {
      this.handleOkButtonClick();
    };
    this.okButton.addActionListener(_function);
    final ActionListener _function_1 = (ActionEvent it) -> {
      this.handleSelectFolderClick();
    };
    this.selectFolderButton.addActionListener(_function_1);
    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.anchor = GridBagConstraints.LINE_START;
    this.add(this.text, constraints);
  }

  /**
   * This function gets the path of the folder selected by the user and the criteria
   * (= extension) selected in the comboBox
   */
  public void handleOkButtonClick() {
    final String path = this.textField.getText();
    final String criteria = this.comboBox.getSelectedItem().toString();
    this.callback.onSearch(path, criteria);
  }

  /**
   * This function triggers the search of a folder into the finder (or file explorer)
   * and stores the path into the textField Widget in order to keep a trace of the folder path
   */
  public void handleSelectFolderClick() {
    final JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    final int returnValue = chooser.showOpenDialog(this);
    if ((returnValue == JFileChooser.APPROVE_OPTION)) {
      final File selectedFile = chooser.getSelectedFile();
      this.textField.setText(selectedFile.getAbsolutePath());
      this.parentGUI.setRootPath(selectedFile.getAbsolutePath());
    }
  }

  /**
   * This function creates a file tree structure starting from a root node.
   * It takes a list of found files and organizes them hierarchically
   * under the specified root path, adding nodes for each file and directory.
   * 
   * @param rootNode: the root node of the tree
   * @param foundFiles: a list of files to be organized in the tree
   * @param rootPath: the root path to start the file tree structure
   */
  public void createFileTree(final DefaultMutableTreeNode rootNode, final ConcurrentLinkedQueue<File> foundFiles, final String rootPath) {
    final HashMap<String, DefaultMutableTreeNode> nodeMap = new HashMap<String, DefaultMutableTreeNode>();
    nodeMap.put(rootPath, rootNode);
    for (final File file : foundFiles) {
      {
        final String absolutePath = file.getAbsolutePath().replace("\\", "/");
        int _length = rootPath.length();
        final String relativePath = absolutePath.substring((_length + 1));
        this.addFilePathToTree(rootNode, relativePath, nodeMap);
      }
    }
  }

  /**
   * This function adds a file path to the tree structure by splitting it into parts.
   * It creates new nodes for each directory and file part of the path that does not already exist in the tree.
   * 
   * @param rootNode: the root node of the tree
   * @param filePath: the path of the file or directory to add
   * @param nodeMap: a map that keeps track of the created nodes by path
   */
  public void addFilePathToTree(final DefaultMutableTreeNode rootNode, final String filePath, final Map<String, DefaultMutableTreeNode> nodeMap) {
    final String[] parts = filePath.split("/");
    DefaultMutableTreeNode currentNode = rootNode;
    String _string = rootNode.toString();
    final StringBuilder currentPath = new StringBuilder(_string);
    for (final String part : parts) {
      {
        currentPath.append("/").append(part);
        boolean _containsKey = nodeMap.containsKey(currentPath.toString());
        if ((!_containsKey)) {
          final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(part);
          currentNode.add(newNode);
          nodeMap.put(currentPath.toString(), newNode);
          currentNode = newNode;
        } else {
          currentNode = nodeMap.get(currentPath.toString());
        }
      }
    }
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
    SearchManagerPanel other = (SearchManagerPanel) obj;
    if (!Objects.equals(this.defaultFoundFilesText, other.defaultFoundFilesText))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.defaultFoundFilesText);
    return result;
  }

  @SyntheticMember
  private static final long serialVersionUID = -3502462864L;

  @Pure
  public String getDefaultFoundFilesText() {
    return this.defaultFoundFilesText;
  }

  @Pure
  public JLabel getText() {
    return this.text;
  }
}
