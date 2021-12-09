package sample.DashBoard;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.hildan.fxgson.FxGson;
import sample.Models.YearlyProduction;
import sample.Models.DeclineModel;
import sample.Toast;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class DeclineModels implements Initializable {
    @FXML
    private Label resultLabel;
    @FXML
    private Label mode;

    int modeSelected = 0;

    public static final int NO_MODE = 0;
    public static final int ED_MODE = 1;
    public static final int H_MODE = 2;
    public static final int GH_MODE = 3;

    private static final int B_ED = 0;
    private static final int B_H = 1;

    ArrayList<DeclineModel> yearlyProductionModels = new ArrayList<>();
    public ArrayList<YearlyProduction> result = new ArrayList<>();

    @FXML
    private Button expDeclineBtn, harmonicBtn, generalHypBtn, analyseBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mode.setText("");
        mode.setText(getActiveModeText());
    }

    @FXML
    private JFXTextField qoJtf, qfJtf, npdJtf, bJtf, aJtf, tfJtf;

    @FXML
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        if (((Node) actionEvent.getSource()).getId().equals("expDeclineBtn")) {
            modeSelected = ED_MODE;
            mode.setText(getActiveModeText());
        } else if (((Node) actionEvent.getSource()).getId().equals("harmonicBtn")) {
            modeSelected = H_MODE;
            mode.setText(getActiveModeText());
        } else if (((Node) actionEvent.getSource()).getId().equals("generalHypBtn")) {
            modeSelected = GH_MODE;
            mode.setText(getActiveModeText());
        } else if (((Node) actionEvent.getSource()).getId().equals("analyseBtn")) {
            yearlyProductionModels.clear();
            result.clear();
            String qo, qf, npd, a, tf, b;
            qo = qoJtf.getText();
            qf = qfJtf.getText();
            npd = npdJtf.getText();
            a = aJtf.getText();
            tf = tfJtf.getText();
            b = bJtf.getText();

            analyseData(qo, qf, npd, a, tf, b);

        }

    }

    private Boolean isNumberAvailable(String string) {
        if (string == null || string.equals("")) {
            return false;
        } else {
            try {
                double d = Double.parseDouble(string);
            } catch (NumberFormatException nfe) {
                Toast.makeText(new Stage(), "Invalid Input", 1500, 500, 500);
                return false;
            }
            return true;
        }
    }

    private void analyseData(String qo, String qf, String npd, String a, String tf, String b) throws BackingStoreException {
        Double qoD, qfD, npdD, aD, tfD, bD;
        switch (modeSelected) {
            case NO_MODE:
                Toast.makeText(new Stage(), "No Mode Selected Click Any Of The Buttons to Select A mode", 1500, 500, 500);
                break;
            case ED_MODE:
                if (isNumberAvailable(qo)) {
                    qoD = Double.parseDouble(qo);
                } else {
                    qoD = null;
                }
                if (isNumberAvailable(qf)) {
                    qfD = Double.parseDouble(qf);
                } else {
                    qfD = null;
                }
                if (isNumberAvailable(npd)) {
                    npdD = Double.parseDouble(npd);
                } else {
                    npdD = null;
                }
                if (isNumberAvailable(a)) {
                    aD = Double.parseDouble(a);
                } else {
                    aD = null;
                }
                if (isNumberAvailable(tf)) {
                    tfD = Double.parseDouble(tf);
                } else {
                    tfD = null;
                }
                analyseExponentialDeclineData(qoD, qfD, npdD, aD, tfD, (double) B_ED);

                break;
            case H_MODE:
                if (isNumberAvailable(qo)) {
                    qoD = Double.parseDouble(qo);
                } else {
                    qoD = null;
                }
                if (isNumberAvailable(qf)) {
                    qfD = Double.parseDouble(qf);
                } else {
                    qfD = null;
                }
                if (isNumberAvailable(npd)) {
                    npdD = Double.parseDouble(npd);
                } else {
                    npdD = null;
                }
                if (isNumberAvailable(a)) {
                    aD = Double.parseDouble(a);
                } else {
                    aD = null;
                }
                if (isNumberAvailable(tf)) {
                    tfD = Double.parseDouble(tf);
                } else {
                    tfD = null;
                }
                analyseHarmonic(qoD, qfD, npdD, aD, tfD, (double) B_H);
                break;
            case GH_MODE:
                if (isNumberAvailable(qo)) {
                    qoD = Double.parseDouble(qo);
                } else {
                    qoD = null;
                }
                if (isNumberAvailable(qf)) {
                    qfD = Double.parseDouble(qf);
                } else {
                    qfD = null;
                }
                if (isNumberAvailable(npd)) {
                    npdD = Double.parseDouble(npd);
                } else {
                    npdD = null;
                }
                if (isNumberAvailable(a)) {
                    aD = Double.parseDouble(a);
                } else {
                    aD = null;
                }
                if (isNumberAvailable(tf)) {
                    tfD = Double.parseDouble(tf);
                } else {
                    tfD = null;
                }
                if (isNumberAvailable(b)) {
                    bD = Double.parseDouble(b);
                } else {
                    bD = null;
                }
                analyseHyperbolic(qoD, qfD, npdD, aD, tfD, bD);
                break;

            default:
                break;
        }

    }

    private String getActiveModeText() {

        switch (modeSelected) {
            case NO_MODE:
                expDeclineBtn.setDisable(false);
                harmonicBtn.setDisable(false);
                generalHypBtn.setDisable(false);
                bJtf.setVisible(false);
                return "No Mode Selected";
            case ED_MODE:
                expDeclineBtn.setDisable(true);
                harmonicBtn.setDisable(false);
                generalHypBtn.setDisable(false);
                bJtf.setVisible(false);
                return "Exponential Decline";
            case H_MODE:
                expDeclineBtn.setDisable(false);
                harmonicBtn.setDisable(true);
                generalHypBtn.setDisable(false);
                bJtf.setVisible(false);
                return "Harmonic";
            case GH_MODE:
                expDeclineBtn.setDisable(false);
                harmonicBtn.setDisable(false);
                generalHypBtn.setDisable(true);
                bJtf.setVisible(true);
                return "General Hyperbolic";

            default:
                return "";
        }

    }

    ArrayList<String> ids;

    private void displayResult(Double qo, Double qf, Double npd, Double a, Double tf, Double b) throws BackingStoreException {
        String resultS = "INITIAL PRODUCTION RATE (Qo): " + qo + "\nECONOMIC LIMIT (Qf): " + qf + "\nCUMULATIVE PRODUCTION (npd): " + npd + "\na: " + a + "\nb: " + b + "\nLIFE OF WELL (Tf): " + tf;

        resultLabel.setText(resultS);

        CustomDialog customDialog = new CustomDialog("Result", resultS, result);

        customDialog.openDialog();
    }


    private static final Interpolator EXP_IN = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
        }
    };

    private static final Interpolator EXP_OUT = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
        }
    };

    private static class CustomDialog extends Stage {

        private ScaleTransition scale1 = new ScaleTransition();
        private ScaleTransition scale2 = new ScaleTransition();

        private SequentialTransition anim = new SequentialTransition(scale1, scale2);

        ArrayList<YearlyProduction> yearlyData;

        CustomDialog(String header, String content, ArrayList<YearlyProduction> yearlyData) {
            Pane root = new Pane();
            this.yearlyData = yearlyData;
            scale1.setFromX(0.01);
            scale1.setFromY(0.01);
            scale1.setToY(1.0);
            scale1.setDuration(Duration.seconds(0.33));
            scale1.setInterpolator(EXP_IN);
            scale1.setNode(root);

            scale2.setFromX(0.01);
            scale2.setToX(1.0);
            scale2.setDuration(Duration.seconds(0.33));
            scale2.setInterpolator(EXP_OUT);
            scale2.setNode(root);

            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);

            Rectangle bg = new Rectangle(500, 400, Color.WHITESMOKE);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(1.5);

            Text headerText = new Text(header);
            headerText.setFont(Font.font(20));

            Text contentText = new Text(content);
            contentText.setFont(Font.font(16));

            VBox box = new VBox(10,
                    headerText,
                    new Separator(Orientation.HORIZONTAL),
                    contentText
            );
            box.setPadding(new Insets(15));

            Button btn = new Button("OK");
            btn.setTranslateX(bg.getWidth() - 50);
            btn.setTranslateY(bg.getHeight() - 50);
            btn.setOnAction(e -> closeDialog());
            Button btn1 = new Button("Show Yearly Production");
            btn1.setTranslateX(bg.getWidth() - 250);
            btn1.setTranslateY(bg.getHeight() - 50);
            btn1.setOnAction(e -> displayYPR());
            Button btn2 = new Button("Show Financial Analysis");
            btn2.setTranslateX(bg.getWidth() - 400);
            btn2.setTranslateY(bg.getHeight() - 50);
            btn2.setOnAction(e -> {
                try {
                    runFinancialAnalysis();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            root.getChildren().addAll(bg, box, btn, btn1, btn2);

            setScene(new Scene(root, null));
        }

        void openDialog() {
            show();

            anim.play();
        }

        void closeDialog() {
            anim.setOnFinished(e -> close());
            anim.setAutoReverse(true);
            anim.setCycleCount(2);
            anim.playFrom(Duration.seconds(0.66));
        }

        private void runFinancialAnalysis() throws IOException {
            Stage stage = new Stage();
            new FinancialAnalysis(yearlyData).start(stage);
            Preferences preferences = Preferences.userNodeForPackage(DeclineModels.class);
            Gson gson = FxGson.create();

            preferences.put("ids", gson.toJson(yearlyData));
            System.out.println("\n\n\n\n"+gson.toJson(yearlyData) +"\n\n\n\n");


            closeDialog();
        }

        void displayYPR() {
            closeDialog();
            String res = "";
            for (int i = 0; i < yearlyData.size(); i++) {
                res += yearlyData.get(i).getYear() + ": Production Per Year ==> " + yearlyData.get(i).getProduction() + "\n";
            }
            CustomDialog customDialog = new CustomDialog("Result", res, yearlyData);
            customDialog.openDialog();
        }
    }

    private void analyseExponentialDeclineData(Double qo, Double qf, Double npd, Double a, Double tf, Double b) throws BackingStoreException {
        if (qo != null && qf != null && npd != null) {
            tf = (npd / (qo - qf)) * Math.log(qo / qf);
            a = tf / (Math.log(qo / qf));
            displayResult(qo, qf, npd, a, tf, b);
            generateDailyProduction(new DeclineModel(qo, qf, npd, a, b, tf,ED_MODE));
        } else if (qo != null && qf != null && npd == null) {
            if (a != null) {
                npd = a * (qo - qf);
                tf = (npd / (qo - qf)) * Math.log(qo / qf);
                displayResult(qo, qf, npd, a, tf, b);
                generateDailyProduction(new DeclineModel(qo, qf, npd, a, b, tf,ED_MODE));
            } else if (tf != null) {
                a = tf / (Math.log(qo / qf));
                npd = a * (qo - qf);
                displayResult(qo, qf, npd, a, tf, b);
                generateDailyProduction(new DeclineModel(qo, qf, npd, a, b, tf,ED_MODE));
            } else {
                Toast.makeText(new Stage(), "Not Enough Data to analyse", 1500, 500, 500);

            }
        } else {
            Toast.makeText(new Stage(), "Not Enough Data to analyse", 1500, 500, 500);
        }
    }

    private void generateDailyProduction(DeclineModel model) {
        yearlyProductionModels.add(model);

        Double initialQ, initialNpd;
        Double finalQ, finalNpd;
        initialQ = model.getQo();
        initialNpd = model.getNpd();
        Double tfMonth = model.getTf() / 365;
        while (initialQ >= model.getQf()) {
            finalNpd = initialNpd - initialQ;
            initialQ = (finalNpd / model.getA()) + model.getQf();
            finalQ = initialQ;
            initialNpd = finalNpd;
            yearlyProductionModels.add(new DeclineModel(finalQ, model.getQf(), finalNpd, model.getA(), model.getB(), 0.0,modeSelected));
            System.out.println(new Gson().toJson(new DeclineModel(finalQ, model.getQf(), finalNpd, model.getA(), model.getB(), model.getTf(),modeSelected)));
        }

        Double initNpd, finNpd;

        int yearAdd = 364;
        initNpd = model.getNpd();
        for (int i = 1; i < Math.ceil(tfMonth) + 1; i++) {
            if (i == Math.ceil(tfMonth)) {
                double multiple = tfMonth - (Math.ceil(tfMonth) - 1);
                int days = ((int) (multiple * 365)) - 1;
                yearAdd = (yearAdd + days) - 365;
                finNpd = yearlyProductionModels.get(yearAdd).getNpd();
                DecimalFormat df = new DecimalFormat("#.#");
                result.add(new YearlyProduction(df.format(tfMonth), (initNpd - finNpd)));
                System.out.println(df.format(tfMonth) + "  Production Per Year: " + (initNpd - finNpd));
            } else {
                finNpd = yearlyProductionModels.get(yearAdd).getNpd();
                result.add(new YearlyProduction(String.valueOf(i), (initNpd - finNpd)));
                System.out.println(i + "  Production Per Year: " + (initNpd - finNpd));
                initNpd = finNpd;
                yearAdd = yearAdd + 365;
            }

        }

    }

    private void generateDailyProductionHarmonic(DeclineModel model) {
        yearlyProductionModels.add(model);

        Double initialQ, initialNpd;
        Double finalQ, finalNpd;
        initialQ = model.getQo();
        initialNpd = model.getNpd();
        Double tfMonth = model.getTf() / 365;
        while (initialQ >= model.getQf()) {
            finalNpd = initialNpd - initialQ;
            initialQ = (finalNpd / (model.getA() * Math.log((1 + (model.getTf() / model.getA())))));
            finalQ = initialQ;
            initialNpd = finalNpd;
            yearlyProductionModels.add(new DeclineModel(finalQ, model.getQf(), finalNpd, model.getA(), model.getB(), 0.0,modeSelected));
            System.out.println(new Gson().toJson(new DeclineModel(finalQ, model.getQf(), finalNpd, model.getA(), model.getB(), model.getTf(),modeSelected)));
        }

        Double initNpd, finNpd;

        int yearAdd = 364;
        initNpd = model.getNpd();
        for (int i = 1; i < Math.ceil(tfMonth) + 1; i++) {
            System.out.println(tfMonth + "  " + Math.ceil(tfMonth) + "  " + i + "   " + yearlyProductionModels.size());
            if (i == Math.ceil(tfMonth)) {
                double multiple = tfMonth - (Math.ceil(tfMonth) - 1);
                int days = ((int) (multiple * 365)) - 1;
                yearAdd = (yearAdd + days) - 365;
                finNpd = yearlyProductionModels.get(yearAdd).getNpd();
                DecimalFormat df = new DecimalFormat("#.#");
                result.add(new YearlyProduction(df.format(tfMonth), (initNpd - finNpd)));
                System.out.println(df.format(tfMonth) + "  Production Per Year: " + (initNpd - finNpd));
            } else {
                finNpd = yearlyProductionModels.get(yearAdd).getNpd();
                result.add(new YearlyProduction(String.valueOf(i), (initNpd - finNpd)));
                System.out.println(i + "  Production Per Year: " + (initNpd - finNpd));
                initNpd = finNpd;
                yearAdd = yearAdd + 365;
            }

        }

    }

    private void generateDailyProductionHyperbolic(DeclineModel model) {
        yearlyProductionModels.add(model);

        Double initialQ, initialNpd;
        Double finalQ, finalNpd;
        initialQ = model.getQo();
        initialNpd = model.getNpd();
        Double tfMonth = model.getTf() / 365;
        while (initialQ >= model.getQf()) {
            finalNpd = initialNpd - initialQ;
            initialQ = ((finalNpd * (1 - model.getB())) / model.getA()) / (1 - Math.pow(1 + ((model.getB() * model.getTf()) / model.getA()), (model.getB() - 1) / model.getB()));
            finalQ = initialQ;
            initialNpd = finalNpd;
            yearlyProductionModels.add(new DeclineModel(finalQ, model.getQf(), finalNpd, model.getA(), model.getB(), 0.0,modeSelected));
            System.out.println(new Gson().toJson(new DeclineModel(finalQ, model.getQf(), finalNpd, model.getA(), model.getB(), model.getTf(),modeSelected)));
        }

        Double initNpd, finNpd;

        int yearAdd = 364;
        initNpd = model.getNpd();
        for (int i = 1; i < Math.ceil(tfMonth) + 1; i++) {
            if (i == Math.ceil(tfMonth)) {
                double multiple = tfMonth - (Math.ceil(tfMonth) - 1);
                int days = ((int) (multiple * 365)) - 1;
                yearAdd = (yearAdd + days) - 365;
                finNpd = yearlyProductionModels.get(yearAdd).getNpd();
                DecimalFormat df = new DecimalFormat("#.#");
                result.add(new YearlyProduction(df.format(tfMonth), (initNpd - finNpd)));
                System.out.println(df.format(tfMonth) + "  Production Per Year: " + (initNpd - finNpd));
            } else {
                finNpd = yearlyProductionModels.get(yearAdd).getNpd();
                result.add(new YearlyProduction(String.valueOf(i), (initNpd - finNpd)));
                System.out.println(i + "  Production Per Year: " + (initNpd - finNpd));
                initNpd = finNpd;
                yearAdd = yearAdd + 365;
            }

        }

    }

    private void analyseHarmonic(Double qo, Double qf, Double npd, Double a, Double tf, Double b) throws BackingStoreException {
        if (qo != null && qf != null && npd != null) {
            tf = (npd / qo) * (((qo / qf) - 1) / Math.log(qo / qf));
            a = tf / ((qo / qf) - 1);
            displayResult(qo, qf, npd, a, tf, b);
            generateDailyProductionHarmonic(new DeclineModel(qo, qf, npd, a, b, tf,modeSelected));
        } else if (qo != null && qf != null && npd == null) {
            if (a != null) {
                npd = a * qo * Math.log(qo / qf);
                tf = (npd / qo) * (((qo / qf) - 1) / Math.log(qo / qf));
                displayResult(qo, qf, npd, a, tf, b);
                generateDailyProductionHarmonic(new DeclineModel(qo, qf, npd, a, b, tf,modeSelected));
            } else if (tf != null) {
                a = tf / ((qo / qf) - 1);
                npd = a * qo * Math.log(qo / qf);
                displayResult(qo, qf, npd, a, tf, b);
                generateDailyProductionHarmonic(new DeclineModel(qo, qf, npd, a, b, tf,modeSelected));
            } else {
                Toast.makeText(new Stage(), "Not Enough Data to analyse", 1500, 500, 500);
            }
        } else {
            Toast.makeText(new Stage(), "Not Enough Data to analyse", 1500, 500, 500);
        }
    }

    private void analyseHyperbolic(Double qo, Double qf, Double npd, Double a, Double tf, Double b) throws BackingStoreException {
        if (b == null) {
            b = 0.5;
        }
        if (b > 1 || b < 0) {
            //invalid date
            return;
        }
        Double u;

        if (qo != null && qf != null && npd != null) {
            u = qo / qf;
            tf = ((1 - b) / b) * ((npd / qo) * Math.pow(u, 1 - b)) * ((Math.pow(u, b) - 1) / (Math.pow(u, 1 - b) - 1));
            a = b * tf / (Math.pow((qo / qf), b) - 1);
            displayResult(qo, qf, npd, a, tf, b);
            generateDailyProductionHyperbolic(new DeclineModel(qo, qf, npd, a, b, tf,modeSelected));
        } else if (qo != null && qf != null && npd == null) {
            u = qo / qf;
            if (a != null) {
                npd = ((a * Math.pow(qo, b)) / (1 - b)) * (Math.pow(qo, 1 - b) - Math.pow(qf, 1 - b));
                tf = ((1 - b) / b) * ((npd / qo) * Math.pow(u, 1 - b)) * ((Math.pow(u, b) - 1) / (Math.pow(u, 1 - b) - 1));
                displayResult(qo, qf, npd, a, tf, b);
                generateDailyProductionHyperbolic(new DeclineModel(qo, qf, npd, a, b, tf,modeSelected));
            } else if (tf != null) {
                a = b * tf / (Math.pow((qo / qf), b) - 1);
                npd = ((a * Math.pow(qo, b)) / (1 - b)) * (Math.pow(qo, 1 - b) - Math.pow(qf, 1 - b));
                displayResult(qo, qf, npd, a, tf, b);
                generateDailyProductionHyperbolic(new DeclineModel(qo, qf, npd, a, b, tf,modeSelected));
            } else {
                Toast.makeText(new Stage(), "Not Enough Data to analyse", 1500, 500, 500);
            }
        } else {
            Toast.makeText(new Stage(), "Not Enough Data to analyse", 1500, 500, 500);
        }
    }
}
