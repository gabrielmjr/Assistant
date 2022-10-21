package com.gabrielMJr.twaire.assistant.data;

import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;

public class DataLoader extends AppCompatActivity
{

    // SP
    private static SharedPreferences sharedPreferences;

    // Those constants are geral names for each question and answer on sharedpreferences
    // This strint contain the numbers of total questions and answers
    private static final String GERAL_SHARED_PREFERENCES = "questions_config";
    private SharedPreferences.Editor editor;

    // Here is the constants of number of questions/answers on the geral SP
    private static final String GERAL_QUESTIONS_ANSWERS = "questions_answers_number";

    // This integer contain the total number of question/answer saved on SP
    private static int total_questions_answers;

    // Here is data from shared preferences
    // Those data are tye questions and answers that you teached te assistant
    private static SharedPreferences QUESTION_SH = null;
    private static final String QUESTION_ID = "question_";
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";

    // Initialize shared preferences
    protected void initializeSharedPreferences()
    {
        sharedPreferences = getApplicationContext().getSharedPreferences(GERAL_SHARED_PREFERENCES, 0);
        editor = sharedPreferences.edit();

        // If the geral_questions_answers is empty, the assistant didnt learn the questions yet
        total_questions_answers = sharedPreferences.getInt(GERAL_QUESTIONS_ANSWERS, 0);
    }

    // Add the questions and answers to new shared preferences file
    protected void addQuestionAnswer(String question, String answer)
    {
        // Updating total answer's number from geral shared preferences
        total_questions_answers++;
        editor = sharedPreferences.edit();
        editor.putInt(GERAL_QUESTIONS_ANSWERS, total_questions_answers);
        editor.commit();

        // new SP = total SP + 1
        QUESTION_SH = getApplicationContext().getSharedPreferences(QUESTION_ID + total_questions_answers, 0);
        editor = QUESTION_SH.edit();

        // Putting new question and answer to new sharedP file
        editor.putString(QUESTION, question);
        editor.putString(ANSWER, answer);
        editor.commit();
    }

    // Get shared preferences questions
    protected String[] getQuestions()
    {
        // Questions will be saved here
        String[] result = new String[total_questions_answers];

        for (int i = 1; i < total_questions_answers ; i++)
        {
            QUESTION_SH = getApplicationContext().getSharedPreferences(QUESTION_ID + i, 0);
            if (QUESTION_SH.getString(QUESTION, null) != null)
            {
                result[i] = QUESTION_SH.getString(QUESTION, null);        
            }
        }

        // Returning result
        return result;
    }

    // Get shared preferences answer
    protected String getAnswer(int question_id)
    {
        QUESTION_SH = getApplicationContext().getSharedPreferences(QUESTION_ID + question_id, 0);
        return QUESTION_SH.getString(ANSWER, null);
    }

    // Get question/answer file index fromm sharedPreferences
    protected int getQuestionIndex(String question)
    {
        for (int i = 1; i < total_questions_answers; i++)
        {
            // Getting all questions strings from SP
            QUESTION_SH = getApplicationContext().getSharedPreferences(QUESTION_ID + i, 0);
            String qs = QUESTION_SH.getString(QUESTION, null);

            // final result is different to null, return index
            if (qs != null)
            {
                if (qs.equals(question))
                {
                    return i;
                }
            }
        }

        // Return result if null
        return -1;
    }

}
