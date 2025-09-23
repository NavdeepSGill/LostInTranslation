package translation;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel countryPanel = new JPanel();

            JSONTranslator translator = new JSONTranslator();
            List<String> countryCodes = translator.getCountryCodes();
            List<String> languageCodes = translator.getLanguageCodes();

            // Convert country codes to country names for dropdown
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            List<String> countryNames = countryCodes
                    .stream()
                    .map(countryCodeConverter::fromCountryCode)
                    .collect(Collectors.toList());
            JComboBox<String> countryField = new JComboBox<>(countryNames.toArray(new String[0]));
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryField);

            // Convert language codes to language names for scrollable list
            JPanel languagePanel = new JPanel();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            List<String> languageNames = languageCodes
                    .stream()
                    .map(languageCodeConverter::fromLanguageCode)
                    .collect(Collectors.toList());

            // Create a scrollable list for languages
            JList<String> languageList = new JList<>(languageNames.toArray(new String[0]));
            languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane languageScrollPane = new JScrollPane(languageList);
            languageScrollPane.setPreferredSize(new java.awt.Dimension(200, 100));

            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageScrollPane);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedLanguage = languageList.getSelectedValue();
                    String country = (String)countryField.getSelectedItem();

                    String countryCode = countryCodeConverter.fromCountry(country);
                    String languageCode = languageCodeConverter.fromLanguage(selectedLanguage);

                    if (selectedLanguage == null) {
                        resultLabel.setText("Please select a language!");
                        return;
                    }

                    // for now, just using our simple translator, but
                    // we'll need to use the real JSON version later.
                    Translator translator = new JSONTranslator();

                    String result = translator.translate(countryCode, languageCode);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
