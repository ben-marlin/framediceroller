# Project 16: Dice Roller App

I'm sure you're tired of rolling dice by now, but it's an easy example that lets you look back at what we've done previously.

## Sample Code

The program `BaseDiceRoller.java` is a solution to one of the first dice rolling program we wrote earlier in the semester. Run it and read the code to make sure you remember how it works. 

It only runs once, and it's a pain to click in the terminal pane every time. 

## Improving on the Sample

We would like an app where we could specify the number of dice to roll and the type of dice. We'll build a frame with panels. This involves the following.

- frame & main method
- panels
- GridLayout
- text fields
- labels
- a button
- ActionListener

## Build the Class

Start this the same way you did the JFrame graphics example. `File > New File > New Java File > Class`. I named mine `DiceRollerFrame.java`, but if you name it something different, you'll need to make the appropriate adjustments.

Add the package line, and comment your name. You'll need the following imports.
```
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
```
Change the class signature to extend JFrame. Create an empty constructor that we'll fill in shortly. Create a main method that only contains the command `new DiceRollerFrame();`. See if it will run with so little mechanism in place.

## Common Fields

The JFrame is going to need several fields or objects which will need to be accessed in multiple methods, so we declare them at the top of the class, before the constructor.
```
private JTextField field1;
private JLabel label1;
private JTextField field2;
private JLabel label2;
private JButton rollButton;
```

## Basic Constructor

Add the following code to the constructor.
```
// set up frame
setTitle("Dice Roller");
setSize(300, 200);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
setLayout(new BorderLayout());

setVisible(true); // keep this at the end of the constructor
```
The purpose of most of these commands are self-explanatory, but setLocationRelativeTo(null) would have some effect on mouse events, but not much else. Experiment with deleting it to see if it still compiles.

## Adding Textfields and Labels

We're going to use textfields for input and labels for output. To that end, add the following code after setting the grid layout.
```
// Create components
field1 = new JTextField("3", 5);
label1 = new JLabel("d");
field2 = new JTextField("6", 5);
label2 = new JLabel("=14");
rollButton = new JButton("Roll Dice");
```
We will use field1 to hold the number of dice and field2 to hold the size of the dice. The d in label1 between them helps the user know they're rolling 3d6 or 4d10 or whatever. We will use label2 to display the roll result, and initially we just want something displayed.

## Using a Panel with a Layout

As previously discussed, a panel is just a container to put other components into. Add the following after instantiating the components.
```
// Create panel and add components
JPanel panel = new JPanel();
panel.setLayout(new FlowLayout());
panel.add(field1);
panel.add(label1);
panel.add(field2);
panel.add(label2);
```
The new piece here is the FlowLayout. It just adds the components left to right until it runs out of room, and then starts a new row. When the window is resized, it redoes this. It's very reasonable for this purpose, but I think the final result is a little clunky. Other options would have made the fields grow in height to fill a pane, though.

## Adding the Button

We need a button to make the roller work. Add the following code after the panel adds.
```
// Add panel and button to frame
add(panel, BorderLayout.CENTER);
add(rollButton, BorderLayout.SOUTH);

setVisible(true); // keep this at the end of the constructor
```
Because we add the panel to the CENTER, it should expand across the whole frame. The button, not having a container, will fill the whole SOUTH pane as well.

At this point, you should be able to test your code and see if it looks right. The button won't do anything, but you can at least see if it all looks about right.

## Attaching an ActionListener

Since we need the button to actually do something, we add the following code before setting the visibility for the frame.
```
// Add button action listener
rollButton.addActionListener(new ButtonListener());
```
As written, this will cause an error. This is because we don't have a ButtonListener class defined yet. So after the end of the constructor but before the beginning of the main method, add the following.
```
private class ButtonListener implements ActionListener  {
    @Override
    public void actionPerformed(ActionEvent e) {
        label2.setText("rolled");
    }
}
```
In the actionPerformed method, I just put in a dummy action so we could test it. Run the program and make sure it changes label2 as promised.

## Making the ActionListener Work

At this point, we could use the guts of rollDice from DiceRoller inside actionPerformed. But in keeping with the philosophy of using methods, instead replace the label2.setText line with `rollDice();` and write the rollDice method separately.

For now, insert the following method between the constructor and the main method.
```
private void rollDice() {
    label2.setText("worked!");
}
```
This allows you to check the functionality before sorting out how rollDice should actually work.

## Re-Using rollDice(num,size)

**DO NOT INSERT THE FOLLOWING, IT IS FOR REFERENCE ONLY.**
```
public static int rollDice(int num, int size) {
    Random rand = new Random();
    int sum = 0;
    for (int i = 0; i < num; i++) {
        sum += rand.nextInt(size)+1;
    }
    return sum;
}
```
Our new rollDice can't use this code directly. For one thing, you're not receiving the integers num and size, but instead reading them out of the textfields. So change the code of rollDice to this.
```
private void rollDice() {
    int num = Integer.parseInt(field1.getText());
    int size = Integer.parseInt(field2.getText());

    label2.setText(num + "d" + size);
}
```
This will probably give you an error - try not to worry, we're going to fix it. Seeing the error helps you understand variable types.

If you did not get an error, try running it and typing some letters into one of the fields before pressing the button. That will almost definitely cause an error.

## Error Handling

This is one of the weirdest bits in Java. I'm sure there's something similar in other languages, but I haven't seen it. When you have some code that you expect to cause errors, you can enclose it in a try / catch routine like this.
```
try {
    <some code that won't work>
} catch (expected error type) {
    <what to do when it doesn't>
}
```
The error you expect here is that converting the text to an integer didn't work. So we rewrite our method to this.
```
private void rollDice() {
    try {
        int num = Integer.parseInt(field1.getText());
        int size = Integer.parseInt(field2.getText());

        label2.setText(num + "d" + size);
    } catch (NumberFormatException ex) {
        System.out.println("Well that didn't work.");   
    }
}
```
Test this out. It should allow you to ignore at least that sort of error. It's a little primitive, as it sends the error message to the terminal and the user may not be watching the terminal. Let's fix that by showing an error message in a pop-up window. Replace the println statement with the following.
```
JOptionPane.showMessageDialog(this, "Please enter valid integers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
```
Test this out by running it and entering letters or symbols in a textfield before hitting the button.

## Actually Rolling the Dice

By borrowing code from BaseDiceRoller, we can replace the contents of the try block with the following code.
```
int num = Integer.parseInt(field1.getText());
int size = Integer.parseInt(field2.getText());

Random random = new Random();
int sum = 0;

for (int i = 0; i < num; i++) {
    sum += random.nextInt(size) + 1;
}

label2.setText("=" + sum);
```
All this code should be old hat for you. The only new parts are dealing with the fields and labels.

Test your code. Try a few values. In particular, try entering negative numbers to the textfields. Does it make sense to get 0 from that? For that matter, does it make sense to even bother trying when those are negative?

If you'd like to do an extra step of *data validation*, you could insert the following after the parseInt lines, but before making the randomizer.
```
if (num <= 0 || size <= 0) {
    JOptionPane.showMessageDialog(this, "Please enter positive integers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
    return;
}
```
This uses another pop-up to display an error message. If you have trouble getting this to work, though, it's OK to skip it.

At this point, you should have a working dice roller. You might experiment with layouts to get it to look better, but it should be functional!

## Embellishments

Play with this and find out how to do things. Maybe try Googling "java swing components methods" if you want to learn some more tricks. 

- Can you change the font of a button? If so, button.setFont(new Font("Serif",Font.BOLD,10)) in the constructor should change it.
- Can you change the color of a textfield or label? Try label1.setBackground(Color.PINK) and see what happens. Maybe field1, too?
- We used a GridLayout on a recent project. Try changing the layout on your panel. You'll have to count the number of components.
- Try attaching an ActionListener to the fields. If it works, the app will roll dice when you hit enter in a textfield.
- Certainly put your name on the frame's title bar and jazz things up a little.

## Wrapping Up

As always, save it, stage it, type a commit message, hit the commit button, hit the sync button.

Take a screenshot of a working model and paste it into Canvas. Copy the URL of your repo on GitHub and paste it into Canvas.
