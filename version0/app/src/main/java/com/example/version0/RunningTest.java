package com.example.version0;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import android.Manifest;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunningTest extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private TextView studentNameView;
    private TextView answerView;
    private String currentTestName;
    private Mat qr_2;
    private QRCodeDetector qrCodeDetector;

    private static final String TAG = "OCVSample::Activity";
    private TextView questionTextView;
    private Button nextQuestionBtn;
    private List<QuestionData> questions;
    private int currentQuestionIndex = 0;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private JavaCameraView cameraView;
    CameraBridgeViewBase cameraBridgeViewBase;
    private CameraBridgeViewBase mOpenCvCameraView;

    // Добавляем поле options
    private ArrayList<OptionData> options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV initialization failed!");

            Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(R.layout.activity_running_test);
        studentNameView = findViewById(R.id.studentNameView);
        answerView = findViewById(R.id.answerView);

        getPermission();

        cameraBridgeViewBase = findViewById(R.id.cameraView);
        questionTextView = findViewById(R.id.questionText);
        nextQuestionBtn = findViewById(R.id.nextQuestion);

//        textView = findViewById(R.id.textView);
        qrCodeDetector = new QRCodeDetector();

        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        if (OpenCVLoader.initDebug()) {
            cameraBridgeViewBase.enableView();
        }

        String groupName = getIntent().getStringExtra("groupName");
        String testName = getIntent().getStringExtra("testName");

        // Инициализируем options в методе initializeComponents
        initializeComponents(groupName, testName);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Mat points = new Mat();

        // Обнаружение QR-кодов
        String result = qrCodeDetector.detectAndDecode(rgba, points);

        if (!result.isEmpty()) {
            // Найден QR-код, рисуем точки
            List<Point> pointList = new ArrayList<>();
            for (int i = 0; i < points.cols(); i++) {
                double[] pointData = points.get(0, i);
                Point point = new Point(pointData[0], pointData[1]);
                pointList.add(point);
            }

            // Вычисляем угол наклона QR-кода
            int[] angleCoordinates = calculateQRCodeAngle(pointList);
            int angle = calculateAngle(angleCoordinates[0], angleCoordinates[1]);
            String letter = Letter_ansv(result, angle);

            // После получения и обработки данных QR-кода
            String studentName = result.substring(5,result.length()-1);
            String testName = currentTestName; // Замените на реальное имя теста
            int questionID = currentQuestionIndex + 1; // Номер вопроса (индекс начинается с 0)

            // Получаем правильный вариант ответа
            String correctOptionText = getCorrectOptionText(options);

            // Получаем ответ на основе буквы и списка вариантов ответов
            String answer = getAnswerForLetter(letter, options);

            // Сравниваем полученный ответ с правильным вариантом и записываем результат
            boolean option = answer.equals(correctOptionText);
            System.out.println(angle);
            System.out.println(letter);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Обновление TextView с использованием переменной letter
                    studentNameView.setText(studentName);
                    answerView.setText(answer);
                    if(option){
                        answerView.setTextColor(ContextCompat.getColor(RunningTest.this, R.color.green));
                    } else {
                        answerView.setTextColor(ContextCompat.getColor(RunningTest.this, R.color.red));
                    }
                }
            });
            // Запись ответа в файл
//            AssetManagerUtil.writeResponseToJSON(this, "db_responses.json", studentName, testName, questionID, answer, option);
            for (Point point : pointList) {
                Imgproc.circle(rgba, point, 5, new Scalar(255, 0, 0), -1);
            }
        }

        return rgba;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.enableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    void getPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPermission();
        }
    }

    private void initializeComponents(String groupName, String testName) {
        currentTestName = testName;
        TestSet selectedTestSet = getTestSetByName(testName);
        if (selectedTestSet != null) {
            questions = selectedTestSet.getTests().get(testName);

            showQuestion(currentQuestionIndex);

            // Получаем список вариантов ответов для текущего вопроса
            options = getOptionsForTest(testName, currentQuestionIndex + 1);
        } else {
            questionTextView.setText("Test not found");
        }

        Button nextQuestionButton = findViewById(R.id.nextQuestion);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    showQuestion(currentQuestionIndex);

                    // Получаем список вариантов ответов для следующего вопроса
                    options = getOptionsForTest(testName, currentQuestionIndex + 1);
                } else {
                    questionTextView.setText("No more questions");
                    nextQuestionButton.setText("Go to Home");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showQuestion(int index) {
        questionTextView.setText(questions.get(index).getQuestion());
    }

    private TestSet getTestSetByName(String testName) {
        // Получаем список всех тестов из JSON-файла
        ArrayList<TestSet> testSets = AssetManagerUtil.readTestsFromJSON(this, "db_tests.json");

        // Проходим по каждому тесту и ищем тест с заданным именем
        for (TestSet testSet : testSets) {
            if (testSet.getTests().containsKey(testName)) {
                // Если найден тест с заданным именем, возвращаем его
                return testSet;
            }
        }

        // Если тест не найден, возвращаем null
        return null;
    }

    private int[] calculateQRCodeAngle(List<Point> pointList) {
        int[] angleCoordinates = new int[2];
        if (pointList.size() == 4) {
            // Находим две точки, соответствующие верхнему краю QR-кода
            Point topLeft = null;
            Point topRight = null;
            double maxY1 = Double.MIN_VALUE;
            double maxY2 = Double.MIN_VALUE;

            for (Point point : pointList) {
                if (point.y > maxY1) {
                    maxY2 = maxY1;
                    maxY1 = point.y;
                    topRight = topLeft;
                    topLeft = point;
                } else if (point.y > maxY2) {
                    maxY2 = point.y;
                    topRight = point;
                }
            }

            angleCoordinates[0] = (int) topLeft.x;
            angleCoordinates[1] = (int) topRight.x;
        }
        return angleCoordinates;
    }

    private String Letter_ansv(String decode_text, int angle) {
        if (angle == 90) {
            return String.valueOf(decode_text.charAt(1));
        } else if (angle == -90) {
            return String.valueOf(decode_text.charAt(3));
        } else {
            return "Unknown";
        }
    }

    private int calculateAngle(int x1, int x2) {
        // Вычисляем угол наклона между верхними краями QR-кода и горизонтали
        return (int) Math.toDegrees(Math.atan2(x2 - x1, 0));
    }

    // Метод для получения списка вариантов ответов для заданного теста и вопроса
    private ArrayList<OptionData> getOptionsForTest(String testName, int questionID) {
        ArrayList<TestSet> testSets = AssetManagerUtil.readTestsFromJSON(this, "db_tests.json");
        for (TestSet testSet : testSets) {
            if (testSet.getTests().containsKey(testName)) {
                List<QuestionData> questions = testSet.getTests().get(testName);
                if (questionID <= questions.size()) {
                    // Используем ArrayList для создания копии списка вариантов ответов
                    return new ArrayList<>(questions.get(questionID - 1).getOptions());
                }
            }
        }
        return new ArrayList<>();
    }


    // Метод для получения текста правильного варианта ответа
    private String getCorrectOptionText(ArrayList<OptionData> options) {
        for (OptionData option : options) {
            if (option.isCorrect()) {
                return option.getOption();
            }
        }
        return "";
    }

    // Метод для определения ответа на основе буквы и списка вариантов ответов
    private String getAnswerForLetter(String letter, List<OptionData> options) {
        switch (letter) {
            case "A":
                return options.get(0).getOption();
            case "B":
                return options.get(1).getOption();
            case "C":
                return options.get(2).getOption();
            case "D":
                return options.get(3).getOption();
            default:
                return "";
        }
    }
}
