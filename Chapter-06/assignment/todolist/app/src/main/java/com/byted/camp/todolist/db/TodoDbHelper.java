package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库
    private static int DATABASE_VERSION = 1;

    public TodoDbHelper(Context context) {
        super(context, "todo.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i <= newVersion; i++) {
            if (i == 1) {
                try {
                    db.execSQL(
                            "ALTER TABLE " + TodoContract.TodoEntry.TABLE_NAME +
                            " ADD " + TodoContract.TodoEntry.COLUMN_NAME_PRIORITY + " INTEGER DEAFUALT 0"
                    );
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
