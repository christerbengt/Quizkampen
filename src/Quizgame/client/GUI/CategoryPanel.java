package Quizgame.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CategoryPanel extends JFrame implements ActionListener {

    /*
Discalaimer,
The code for the category panel and the question panel is almost the same, so any all coments will stay
in the category method for simplicity.

It should be doable to do a singel method for both, but I haven't figured out how to do it yet.

it became a bit convoluded, beacuse I had to add a sub panel into each subpanel. Think about this
like russian dolls. Could not figure out a way around this.

 */

    static JPanel createCategoryPanel(String category1, String category2, String category3) {
        //create buttons
        JButton categoryButton1 = new JButton(category1);
        JButton categoryButton2 = new JButton(category2);
        JButton categoryButton3 = new JButton(category3);


        //create panels
        JPanel mainPanel = new JPanel();
        JPanel centerPanel1 = new JPanel();
        JPanel centerPanel2 = new JPanel();
        JPanel answerPanel = new JPanel();
        JPanel questionPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        ArrayList<JButton> answerButtons =new ArrayList<>();
        answerButtons.add(categoryButton1);
        answerButtons.add(categoryButton2);
        answerButtons.add(categoryButton3);

        int sizeOfCategoryButtons = answerButtons.size();

        //create answer panel and add buttons
        answerPanel.setLayout(new GridLayout(sizeOfCategoryButtons,1,20,20));
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(answerPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        for (JButton button : answerButtons) {
            button.addActionListener(new QuizCampenGUI.NextPanelAction()); //dessa måste göra något, just nu går dom bara vidare
            answerPanel.add(button);
        }

        centerPanel2.setLayout(new GridBagLayout());
        centerPanel2.add(questionPanel);
        questionPanel.setLayout(new FlowLayout(100, 100, 100));
        JLabel questionLabel = new JLabel("Pick a category:", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        questionPanel.add(questionLabel);
        centerPanel2.add(questionPanel);
        mainPanel.add(centerPanel2, BorderLayout.NORTH);

        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
