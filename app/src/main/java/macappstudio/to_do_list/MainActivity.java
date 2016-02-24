package macappstudio.to_do_list;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListToDo);
        ListView listViewToDo = (ListView) findViewById(R.id.ListViewToDo);
        listViewToDo.setAdapter(arrayAdapterToDo);

        registerForContextMenu(listViewToDo);

        try {
            Log.i("ON CREATE", "Hi, the on create has occured.");

            Scanner scanner = new Scanner(openFileInput("ToDo.txt"));

            while (scanner.hasNextLine()) {
                String todo = scanner.nextLine();
                arrayAdapterToDo.add(todo);
            }

            scanner.close();
        } catch (Exception e) {
            Log.i("ON CREATE", e.getMessage());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.ListViewToDo) {
            return;
        }

        menu.setHeaderTitle("What would you like to do?");
        String[] options = { "Delete Task","Return" };

        for (String option : options) {
            menu.add(option);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex = info.position;

        if (item.getTitle().equals("Delete Task")) {
            arrayListToDo.remove(selectedIndex);
            arrayAdapterToDo.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        try {
            Log.i("ON BACK PRESSED", "hi, the on back pressed event is occured.");

            PrintWriter pw = new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));

            for (String todo : arrayListToDo) {
                pw.println(todo);
            }

            pw.close();

        } catch (Exception e){
            Log.i("ON BACK PRESSED", e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void buttonAddClick(View v) {
        EditText editTextToDo = (EditText)findViewById(R.id.editTextToDo);
        String todo = editTextToDo.getText().toString().trim();

        if(todo.isEmpty()) {
            return;
        }

        arrayAdapterToDo.add(todo);
        editTextToDo.setText("");

    }
}
