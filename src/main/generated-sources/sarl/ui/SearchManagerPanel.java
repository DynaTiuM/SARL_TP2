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
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class SearchManagerPanel extends JPanel {
  private final JTextField textField = new JTextField(15);

  private final LinkedList<String> extensions = CollectionLiterals.<String>newLinkedList(".sarl", ".txt");

  private final JComboBox<String> comboBox = new JComboBox<String>(((String[])Conversions.unwrapArray(this.extensions, String.class)));

  private final JButton okButton = new JButton("OK");

  private final JButton selectFolderButton = new JButton("Select Folder");

  private final SearchManagerCallback callback;

  private final SearchManagerGUI parentGUI;

  public SearchManagerPanel(final SearchManagerCallback callback, final SearchManagerGUI parentGUI) {
    this.callback = callback;
    this.parentGUI = parentGUI;
    GridBagLayout _gridBagLayout = new GridBagLayout();
    this.setLayout(_gridBagLayout);
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
  }

  public void handleOkButtonClick() {
    final String path = this.textField.getText();
    final String criteria = this.comboBox.getSelectedItem().toString();
    this.callback.onSearch(path, criteria);
  }

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

  public void createFileTree(final DefaultMutableTreeNode rootNode, final ConcurrentLinkedQueue<File> foundFiles, final String rootPath) {
    final HashMap<String, DefaultMutableTreeNode> nodeMap = new HashMap<String, DefaultMutableTreeNode>();
    nodeMap.put(rootPath, rootNode);
    for (final File file : foundFiles) {
      {
        final String absolutePath = file.getAbsolutePath();
        int _length = rootPath.length();
        final String relativePath = absolutePath.substring((_length + 1));
        InputOutput.<String>println(relativePath);
        this.addFilePathToTree(rootNode, relativePath, nodeMap);
      }
    }
  }

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
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    return result;
  }

  @SyntheticMember
  private static final long serialVersionUID = -2782675538L;
}
