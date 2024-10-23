package ui;

import holarchy.SearchManagerCallback;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class SearchManagerGUI extends JFrame {
  public SearchManagerGUI(final SearchManagerCallback callback) {
    this.setTitle("Search Manager GUI");
    Dimension _dimension = new Dimension(500, 300);
    this.setPreferredSize(_dimension);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final SearchManagerPanel panel = new SearchManagerPanel(callback);
    this.getContentPane().add(panel, BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
    this.requestFocus();
  }

  @SyntheticMember
  private static final long serialVersionUID = -1129560162L;
}
