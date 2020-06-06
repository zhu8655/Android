package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    private TodoDbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        // init database
        dbHelper = new TodoDbHelper(this);
        db = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        db.close();
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans

        if (db == null)
            return null;

        String[] projection = {
                BaseColumns._ID,
                TodoContract.TodoEntry.COLUMN_NAME_DATE,
                TodoContract.TodoEntry.COLUMN_NAME_STATE,
                TodoContract.TodoEntry.COLUMN_NAME_CONTENT,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY
        };

        String sortOrder = TodoContract.TodoEntry.COLUMN_NAME_PRIORITY + " DESC," +
                TodoContract.TodoEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = null;
        List<Note> notes = new ArrayList<>();

        try {
            cursor = db.query(
                    TodoContract.TodoEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry._ID));
                int date = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_DATE));
                int state = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_STATE));
                String text = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_CONTENT));
                int priority = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY));

                Note note = new Note(id);
                note.setContent(text);
                note.setDate(new Date(date));
                note.setState(State.from(state));
                note.setPriority(Priority.from(priority));

                notes.add(note);
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
                return notes;
            }
        }

        return null;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        if (db == null)
            return;

        String selection = TodoContract.TodoEntry._ID + " = ?";
        String[] selectionArgs = {
                String.valueOf(note.id)
        };

        db.delete(
                TodoContract.TodoEntry.TABLE_NAME,
                selection,
                selectionArgs
        );

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    private void updateNode(Note note) {
        // 更新数据
        if (db == null)
            return;

        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_STATE, note.getState().intValue);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DATE, note.getDate().getTime());
        values.put(TodoContract.TodoEntry.COLUMN_NAME_CONTENT, note.getContent());

        String selection = TodoContract.TodoEntry._ID + " = ?";
        String[] selectionArgs = {
                String.valueOf(note.id)
        };

        db.update(
                TodoContract.TodoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

}
