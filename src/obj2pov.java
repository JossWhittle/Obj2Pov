import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class obj2pov {

	public static void main(String[] args) {
		new obj2pov();
	}
	
	public obj2pov() {
		
		try {
			convert();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void convert() throws Exception {
		
		String f = "(\\-?\\d+\\.\\d+)";
		Regex reg_v = new Regex("(?uix)^v\\s+"+f+"\\s+"+f+"\\s+"+f);
		Regex reg_vn = new Regex("(?uix)^vn\\s+"+f+"\\s+"+f+"\\s+"+f);
		Regex reg_vt = new Regex("(?uix)^vt\\s+"+f+"\\s+"+f);
		Regex reg_f = new Regex("(?uix)^f\\s+(\\d+)/(\\d*)/(\\d+)\\s+(\\d+)/(\\d*)/(\\d+)\\s+(\\d+)/(\\d*)/(\\d+)");
		
		String result = "";
		ArrayList<Vector> vertices = new ArrayList<Vector>();
		ArrayList<Vector> normals = new ArrayList<Vector>();
		ArrayList<Vector> texture = new ArrayList<Vector>();
		
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		Scanner input = new Scanner(System.in);
		while (input.hasNext()) {
			
			String lineIn = input.nextLine();
			
			if (reg_v.match(lineIn)) {
				
				String[] str = reg_v.getGroups();
				double x = Double.parseDouble(str[1]);
				double y = Double.parseDouble(str[2]);
				double z = Double.parseDouble(str[3]);
				vertices.add(new Vector(x,y,z));
				
			} else if (reg_vn.match(lineIn)) {
				
				String[] str = reg_vn.getGroups();
				double x = Double.parseDouble(str[1]);
				double y = Double.parseDouble(str[2]);
				double z = Double.parseDouble(str[3]);
				normals.add(new Vector(x,y,z));
				
			} else if (reg_vt.match(lineIn)) {
				
				String[] str = reg_vt.getGroups();
				double x = Double.parseDouble(str[1]);
				double y = Double.parseDouble(str[2]);
				texture.add(new Vector(x,y));
				
			} else if (reg_f.match(lineIn)) {
				
				String[] str = reg_f.getGroups();
				int v0 = Integer.parseInt(str[1]) - 1;
				int v1 = Integer.parseInt(str[4]) - 1;
				int v2 = Integer.parseInt(str[7]) - 1;
				
				int n0 = Integer.parseInt(str[3]) - 1;
				int n1 = Integer.parseInt(str[6]) - 1;
				int n2 = Integer.parseInt(str[9]) - 1;
				
				int t0 = Integer.parseInt(str[2]) - 1;
				int t1 = Integer.parseInt(str[5]) - 1;
				int t2 = Integer.parseInt(str[8]) - 1;
				
				Triangle t = new Triangle(vertices.get(v0),
										   vertices.get(v1),
										   vertices.get(v2),
										   normals.get(n0),
										   normals.get(n1),
										   normals.get(n2),
										   texture.get(t0),
										   texture.get(t1),
										   texture.get(t2));
				
				result += t.toString();
			}
		}
		
		System.out.println(padMesh(result));
	}
	
	private String padMesh(String str) {
		
		return "// This file was generated using Obj2Pov written by Joss Whittle.\n" + 
			   "// For more information on the project see: https://github.com/L2Program/Obj2Pov\n" +
			   "\nmesh {\n" + str + "\n}";
	}
	
	private class Triangle {
		
		private Vector m_v0, m_v1, m_v2, m_n0, m_n1, m_n2, m_t0, m_t1, m_t2;
		
		public Triangle(Vector v0, Vector v1, Vector v2, Vector n0, Vector n1, Vector n2, Vector t0, Vector t1, Vector t2) {
			m_v0 = v0;
			m_v1 = v1;
			m_v2 = v2;
			
			m_n0 = n0;
			m_n1 = n1;
			m_n2 = n2;
			
			m_t0 = t0;
			m_t1 = t1;
			m_t2 = t2;
		}
		
		public Vector getV0() { 
			return m_v0;
		}
		
		public Vector getV1() { 
			return m_v1;
		}
		
		public Vector getV2() { 
			return m_v2;
		}
		
		public Vector getN0() { 
			return m_n0;
		}
		
		public Vector getN1() { 
			return m_n1;
		}
		
		public Vector getN2() { 
			return m_n2;
		}
		
		public Vector getT0() { 
			return m_t0;
		}
		
		public Vector getT1() { 
			return m_t1;
		}
		
		public Vector getT2() { 
			return m_t2;
		}
		
		public String toString() {
			
			return "\n\tsmooth_triangle {\n\t\t" + getV0().toString() + ", " + getN0().toString() + ", " +
									  "\n\t\t" + getV1().toString() + ", " + getN1().toString() + ", " +
									  "\n\t\t" + getV2().toString() + ", " + getN2().toString() +
									  "\n\t\tuv_vectors " + getT0().get2D() + ", " + getT1().get2D() + ", " + getT2().get2D() +
				   "\n\t}";
		}
	}
	
	private class Vector {
		
		private double m_x, m_y, m_z;
		
		public Vector(double x, double y) {
			m_x = x;
			m_y = y;
			m_z = 0;
		}
		
		public Vector(double x, double y, double z) {
			m_x = x;
			m_y = y;
			m_z = z;
		}
		
		public double getX() { 
			return m_x;
		}
		
		public double getY() { 
			return m_y;
		}
		
		public double getZ() { 
			return m_z;
		}
		
		public String toString() {
			
			return "<" + m_x + ", " + m_y + ", " + m_z + ">";
		}
		
		public String get2D() {
			
			return "<" + m_x + ", " + m_y + ">";
		}
	}
	
	private class Regex {

		private Pattern m_pattern;
		private Matcher m_matcher;
		private String[] m_groups;

		public Regex(String reg) {
			m_pattern = Pattern.compile(reg);
		}

		public boolean match(String line) {
			m_matcher = m_pattern.matcher(line);
			return m_matcher.find();
		}

		public String[] getGroups() {
			m_groups = new String[m_matcher.groupCount()+1];
			for (int i = 0; i < m_groups.length; i++) {
				m_groups[i] = m_matcher.group(i);
			}
			return m_groups;
		}

	}

}
