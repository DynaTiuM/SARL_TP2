package ui;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

  private LinkedList<String> extensions = CollectionLiterals.<String>newLinkedList(".sarl", ".txt");

  private final JComboBox<String> comboBox = new JComboBox<String>(((String[])Conversions.unwrapArray(this.extensions, String.class)));

  private final JButton okButton = new JButton("OK");

  public SearchManagerPanel() {
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
    constraints.gridx = 1;
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.CENTER;
    this.add(this.okButton, constraints);
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
  private static final long serialVersionUID = -6281750691L;
}
