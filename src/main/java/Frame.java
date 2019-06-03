import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Frame extends JFrame implements ActionListener {
    TextSearcher textSearcher = new TextSearcher();

    static JFrame jFrame = getFrame();
    static JPanel jPanel = new JPanel();
    JButton buttonChooseFolder = new JButton("Выбрать папку");
    JButton buttonSetExpansion = new JButton("Выбрать расширение");
    JButton buttonSearch = new JButton("Найти");
    JButton buttonPrevious = new JButton("Назад");
    JButton buttonNext = new JButton("Вперёд");
    JTextArea textAreaToSearch = new JTextArea("Ищем", 10, 20);
    JTextArea textAreaFilesFound = new JTextArea("Найдено в файлах:", 10, 20);
    JTextArea textAreaFileContent = new JTextArea("Нашлось", 20, 50);
    JLabel labelPath = new JLabel(textSearcher.directoryPath);
    JLabel labelExtension = new JLabel("  .log");

    JTabbedPane tabbedPaneFilesContent = new JTabbedPane();


    public void actionPerformed(ActionEvent e) {
        JButton choice = (JButton) e.getSource();
        if (choice == buttonChooseFolder) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(textSearcher.directoryPath));

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int folder = fileChooser.showOpenDialog(jPanel);
            if (folder == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().toString();
                System.out.println(path);
                labelPath.setText(path);
                textSearcher.setPath(path);
            }
        } else if (choice == buttonSetExpansion) {
            String extension = JOptionPane.showInputDialog(Frame.this, "Введите расширение файла"); //TODO сделать проверку на правильный ввод
            textSearcher.setExtension(extension);
            labelExtension.setText(" " + extension);
        } else if (choice == buttonSearch) {
            textSearcher.clearFiles();
            textSearcher.searchText(textAreaToSearch.getText());
            textAreaFilesFound.setText("");
            textAreaFilesFound.insert(textSearcher.filesToString(), 0);
            for (File file : textSearcher.files) {
                String content = null;
                try {
                    content = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JTextArea jTextArea = new JTextArea(content, 20, 50);
                jTextArea.setLineWrap(true);
                tabbedPaneFilesContent.addTab(file.getPath(), jTextArea); //TODO сделать в JScrollPane
            }
        }
    }

    public Frame() {
        //super("SPLAT test app");
        jFrame.add(jPanel);
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel.setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0;
        constraints.weighty = 0;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        jPanel.add(buttonChooseFolder, constraints);
        constraints.gridy = 1;
        jPanel.add(new JLabel("Выбранная папка:"), constraints);
        constraints.gridx = 1;
        jPanel.add(labelPath, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        jPanel.add(buttonSetExpansion, constraints);
        constraints.gridx = 1;
        jPanel.add(labelExtension, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        jPanel.add(new JLabel("Текст для поиска:"), constraints);
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        jPanel.add(textAreaToSearch, constraints);
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        jPanel.add(buttonSearch, constraints);
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        jPanel.add(textAreaFilesFound, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        jPanel.add(buttonPrevious, constraints);
        constraints.gridx = 3;
        jPanel.add(buttonNext, constraints);
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 10;
        constraints.gridheight = 20;
        jPanel.add(tabbedPaneFilesContent, constraints);
        //jPanel.add(textAreaFileContent, constraints);

        buttonChooseFolder.addActionListener(this);
        buttonSetExpansion.addActionListener(this);
        buttonSearch.addActionListener(this);
        buttonPrevious.addActionListener(this);
        buttonNext.addActionListener(this);


        jPanel.revalidate();
    }

    static JFrame getFrame() {
        JFrame jFrame = new JFrame() {
        };
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameWidth = 1000, frameHeight = 500;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width / 2 - frameWidth / 2,
                dimension.height / 2 - frameHeight / 2, frameWidth, frameHeight);
        return jFrame;
    }
}
