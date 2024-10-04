import org.example.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.ArrayList;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat; // Hamcrest'in assertThat metodu için
import static org.hamcrest.Matchers.instanceOf; // Hamcrest'in instanceOf matcher'ı için
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ResultAnalyzer.class)
public class MainTest {

    Task task1;
    Task task2;
    Task task3;
    TaskData taskData;
    Set<Task> taskSet1;
    Set<Task> taskSet2;
    Set<Task> taskSet3;

    @BeforeEach
    void setUp() {
        task1 = new Task("Java Collections", "Write List Interface",
                "Ann", Status.IN_QUEUE, Priority.LOW);
        task2 = new Task("Java Collections", "Write Set Interface",
                "Ann", Status.ASSIGNED, Priority.MED);
        task3 = new Task("Java Collections", "Write Map Interface",
                "Bob", Status.IN_QUEUE, Priority.HIGH);

        taskSet1 = new HashSet<>();
        taskSet1.add(task1);
        taskSet2 = new HashSet<>();
        taskSet2.add(task2);
        taskSet3 = new HashSet<>();
        taskSet3.add(task3);

        taskData = new TaskData(taskSet1, taskSet2, taskSet3, new HashSet<>());
    }

    @DisplayName("Task sınıfı doğru access modifiers sahip mi")
    @Test
    public void testTaskAccessModifiers() throws NoSuchFieldException {
        Field assigneeFields = task1.getClass().getDeclaredField("assignee");
        Field descriptionsFields = task1.getClass().getDeclaredField("description");
        Field projectFields = task1.getClass().getDeclaredField("project");
        Field priorityFields = task1.getClass().getDeclaredField("priority");
        Field statusFields = task1.getClass().getDeclaredField("status");

        assertEquals(assigneeFields.getModifiers(), 2);
        assertEquals(descriptionsFields.getModifiers(), 2);
        assertEquals(projectFields.getModifiers(), 2);
        assertEquals(priorityFields.getModifiers(), 2);
        assertEquals(statusFields.getModifiers(), 2);
    }

    @DisplayName("Task sınıfı doğru typelara sahip mi")
    @Test
    public void testTaskTypes() {
        assertThat(task1.getAssignee(), instanceOf(String.class));
        assertThat(task1.getDescription(), instanceOf(String.class));
        assertThat(task1.getPriority(), instanceOf(Priority.class));
        assertThat(task1.getProject(), instanceOf(String.class));
        assertThat(task1.getStatus(), instanceOf(Status.class));
    }

    @DisplayName("TaskData sınıfı doğru access modifiers sahip mi")
    @Test
    public void testTaskDataAccessModifiers() throws NoSuchFieldException {
        Field annTasksField = taskData.getClass().getDeclaredField("annsTasks");
        Field bobTasksField = taskData.getClass().getDeclaredField("bobsTasks");
        Field carolTasksField = taskData.getClass().getDeclaredField("carolsTasks");
        Field unassignedTasksField = taskData.getClass().getDeclaredField("unassignedTasks");

        assertEquals(annTasksField.getModifiers(), 2);
        assertEquals(bobTasksField.getModifiers(), 2);
        assertEquals(carolTasksField.getModifiers(), 2);
        assertEquals(unassignedTasksField.getModifiers(), 2);
    }

    @DisplayName("TaskData getTasks method doğru çalışıyor mu ?")
    @Test
    public void testGetTasksMethod() {
        assertEquals(taskData.getTasks("ann"), taskSet1);
        assertEquals(taskData.getTasks("bob"), taskSet2);
        assertEquals(taskData.getTasks("carol"), taskSet3);
    }

    @DisplayName("TaskData getUnion method doğru çalışıyor mu ?")
    @Test
    public void testGetUnionMethod() {
        Set<Task> taskSet = new HashSet<>();
        taskSet.add(task1);
        taskSet.add(task2);

        // Adding a third task to taskSet1 to ensure we have three distinct tasks in the union
        Set<Task> taskSet1 = new HashSet<>();
        taskSet1.add(task1);  // task1
        taskSet1.add(task3);  // task3 (distinct task)

        Set<Task> totals = taskData.getUnion(taskSet1, taskSet);
        assertEquals(totals.size(), 3);  // Expecting 3 distinct tasks in the union
    }

    @DisplayName("TaskData getIntersect() method doğru çalışıyor mu ?")
    @Test
    public void testGetIntersectMethod() {
        Set<Task> taskSet = new HashSet<>();
        taskSet.add(task1);
        taskSet.add(task2);

        Set<Task> intersections = taskData.getIntersection(taskSet1, taskSet);

        assertEquals(intersections.size(), 1);
        assertEquals(intersections.iterator().next(), task1);
    }

    @DisplayName("TaskData getDifference() method doğru çalışıyor mu?")
    @Test
    public void testGetDifferenceMethod() {
        Set<Task> taskSet = new HashSet<>();
        taskSet.add(task1);
        taskSet.add(task2);

        Set<Task> differences = taskData.getDifferences(taskSet1, taskSet);

        // Assert that the difference should be empty since task1 is present in both sets
        assertEquals(0, differences.size());
    }


    @DisplayName("findUniqueWords doğru çalışıyor mu ?")
    @Test
    public void testFindUniqueWordsMethod() {
        Set<String> uniqueWordsSet = StringSet.findUniqueWords();
        assertEquals(uniqueWordsSet.size(), 143);

        List<String> results = new ArrayList<>(uniqueWordsSet); // Convert Set to List
        assertEquals(results.get(0), "a");
        assertEquals(results.get(results.size() - 1), "wrote");
    }

}
