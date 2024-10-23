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
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
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

  public SearchManagerPanel(final SearchManagerCallback callback) {
    this.callback = callback;
    GridBagLayout _gridBagLayout = new GridBagLayout();
    this.setLayout(_gridBagLayout);
    final GridBagConstraints constraints = new GridBagConstraints();
    Insets _insets = new Insets(5, 5, 5, 5);
    constraints.insets = _insets;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_END;
    JLabel _jLabel = new JLabel("Text: ");
    this.add(_jLabel, constraints);
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.LINE_START;
    this.add(this.textField, constraints);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_END;
    JLabel _jLabel_1 = new JLabel("Options: ");
    this.add(_jLabel_1, constraints);
    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_START;
    this.add(this.comboBox, constraints);
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.LINE_END;
    this.add(this.selectFolderButton, constraints);
    constraints.gridx = 1;
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.CENTER;
    this.add(this.okButton, constraints);
    final ActionListener _function = (ActionEvent it) -> {
      this.handleOkButtonClick();
    };
    this.okButton.addActionListener(_function);
    this.selectFolderButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final int returnValue = chooser.showOpenDialog(SearchManagerPanel.this);
        if ((returnValue == JFileChooser.APPROVE_OPTION)) {
          final File selectedFile = chooser.getSelectedFile();
          SearchManagerPanel.this.textField.setText(selectedFile.getAbsolutePath());
        }
      }
    });
  }

  private void handleOkButtonClick() {
    final String path = this.textField.getText();
    final String criteria = this.comboBox.getSelectedItem().toString();
    this.callback.onSearch(path, criteria);
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
  private static final long serialVersionUID = -7939902280L;
}
