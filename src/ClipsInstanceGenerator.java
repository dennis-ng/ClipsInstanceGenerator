import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class ClipsInstanceGenerator {

	final static String[] SKILLS = { "cleaner", "data-entry", "cashier",
		"stock-taking", "clerk" };
	final static String[] DAYS = { "mon", "tue", "wed", "thu", "fri", "sat",
	"sun" };
	DecimalFormat df = new DecimalFormat("0.##");

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder();
		// generateStaff(s);
		s = new StringBuilder();
		generateTimeslot(s);
	}

	private static void generateStaff(StringBuilder s)
			throws FileNotFoundException {
		s.append("(deffacts COMMON::all-staff\n");
		BufferedReader br = new BufferedReader(new FileReader("names_A-C.csv"));
		try {
			for (int i = 0; i < 50; i++) {
				String name;
				name = br.readLine();

				int wage = (int) (Math.random() * (10 - 7 + 1) + 7);
				String skills = SKILLS[i % SKILLS.length] + " "
						+ SKILLS[(i + 1) % SKILLS.length] + " "
						+ SKILLS[(i + 2) % SKILLS.length];
				int age = 20 + (i % 10);
				String phoneNum = "9";
				for (int j = 0; j < 7; j++) {
					phoneNum += (int) (Math.random() * 10 - 1);
				}
				s.append(String.format("(staff (staffname %s) (wage %d) "
						+ "(skills %s) (age %d) (phone %s))\n", name,
						wage, skills, age, phoneNum));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		s.append(")");
	}

	private static void generateTimeslot(StringBuilder s)
			throws IOException {
		double chanceOfWorking = 0.2;
		s.append(";; Chance of working = " + chanceOfWorking);
		s.append("(deffacts COMMON::staff-choose-availability\n");
		BufferedReader br = new BufferedReader(new FileReader("names_A-C.csv"));
		try {
			for (int i = 0; i < 50; i++) {
				String name;
				name = br.readLine();
				for (String day : DAYS) {
					for (int time = 0; time < 24; time++) {// start times
						if (Math.random() < chanceOfWorking) {
							int startTime = time;
							int endTime = time + 1;
							s.append(String
									.format("(availableslot (staffname %s) (start %d) "
											+ "(end %d) (day %s))\n", name,
											startTime, endTime, day));
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		s.append(")");
		File newFile = new File("availableslot-facts.clp");
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
		bw.write(s.toString());
		bw.close();
	}
}
