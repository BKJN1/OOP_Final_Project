package services;

import java.util.List;
import users.Student;

public class ReportService {
    public String generateMarksReport(List<Student> students) {
        double avgGpa = students.stream().mapToDouble(s -> s.getTranscript().getGpa()).average().orElse(0.0);
        long excellent = students.stream().filter(s -> s.getTranscript().getGpa() >= 3.5).count();
        return "Academic report: students=" + students.size()
                + ", average GPA=" + String.format("%.2f", avgGpa)
                + ", excellent students=" + excellent;
    }
}
