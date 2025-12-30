import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.AssignmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssignmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    private List<Assignment> assignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        // 1. Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Create Dummy Data (Test Data)
        assignmentList = new ArrayList<>();
        assignmentList.add(new Assignment("Math Homework 1", "Oct 25", "Algebra", 100, true));
        assignmentList.add(new Assignment("Physics Lab Report", "Oct 27", "Physics", 50, true));
        assignmentList.add(new Assignment("History Essay", "Nov 02", "World History", 100, true));
        assignmentList.add(new Assignment("Chemistry Quiz", "Nov 05", "Chemistry", 20, true));

        // 3. Initialize Adapter
        adapter = new AssignmentAdapter(assignmentList);

        // 4. Set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
    }
}