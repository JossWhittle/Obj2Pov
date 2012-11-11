import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class obj2pov {

	public static void main(String[] args) {
		new obj2pov(args);
	}
	
	public obj2pov(String[] args) {
		
		try {
			convert((args.length > 0 && args[0].equals("--smooth")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void convert(boolean smooth) throws Exception {
		
		System.out.println("// This file was generated using Obj2Pov written by Joss Whittle.\n" + 
				   		   "// For more information on the project see: https://github.com/L2Program/Obj2Pov\n" +
				   		   "\nmesh {\n");
		
		ArrayList<Vector> vertices = new ArrayList<Vector>();
		ArrayList<Vector> normals = new ArrayList<Vector>();
		ArrayList<Vector> texture = new ArrayList<Vector>();
		
		Scanner input = new Scanner(System.in);
		while (input.hasNext()) {
			
			String lineIn = input.nextLine();
			
			if (lineIn.startsWith("v ")) {
				
				String[] str = lineIn.split(" ");
				vertices.add(new Vector(str[1],str[2],str[3]));
				
				//System.out.println("// Vector " + str[1] + ", " + str[2] + ", " + str[3]);
				
			} else if (lineIn.startsWith("vn ")) {
				
				String[] str = lineIn.split(" ");
				normals.add(new Vector(str[1],str[2],str[3]));
				
				//System.out.println("// Normal " + str[1] + ", " + str[2] + ", " + str[3]);
				
			} else if (lineIn.startsWith("vt ")) {
				
				String[] str = lineIn.split(" ");
				texture.add(new Vector(str[1],str[2]));
				
				//System.out.println("// Texture " + str[1] + ", " + str[2]);
				
			} else if (lineIn.startsWith("f ")) {
				
				String[] s = lineIn.substring(2).split(" ");
				String[] str = s[0].split("/");
				int v0 = Integer.parseInt(str[0]) - 1;
				int t0 = Integer.parseInt(str[1]) - 1;
				int n0 = Integer.parseInt(str[2]) - 1;
				
				str = s[1].split("/");
				int v1 = Integer.parseInt(str[0]) - 1;
				int t1 = Integer.parseInt(str[1]) - 1;
				int n1 = Integer.parseInt(str[2]) - 1;
				
				str = s[2].split("/");
				int v2 = Integer.parseInt(str[0]) - 1;
				int t2 = Integer.parseInt(str[1]) - 1;
				int n2 = Integer.parseInt(str[2]) - 1;
				
				Triangle t = new Triangle(vertices.get(v0),
										   vertices.get(v1),
										   vertices.get(v2),
										   normals.get(n0),
										   normals.get(n1),
										   normals.get(n2),
										   texture.get(t0),
										   texture.get(t1),
										   texture.get(t2));
				
				System.out.println(!smooth ? t.toString() : t.getSmooth());
			}
		}
		
		System.out.println("\n}");
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
			
			return "\n\ttriangle {\n\t\t" + getV0().toString() + ", " + getV1().toString() + ", " + getV2().toString() +
								 "\n\t\tuv_vectors " + getT0().get2D() + ", " + getT1().get2D() + ", " + getT2().get2D() +
				   "\n\t}";
		}
		
		public String getSmooth() {
			
			return "\n\tsmooth_triangle {\n\t\t" + getV0().toString() + ", " + getN0().toString() + ", " +
									  "\n\t\t" + getV1().toString() + ", " + getN1().toString() + ", " +
									  "\n\t\t" + getV2().toString() + ", " + getN2().toString() +
									  "\n\t\tuv_vectors " + getT0().get2D() + ", " + getT1().get2D() + ", " + getT2().get2D() +
				   "\n\t}";
		}
	}
	
	private class Vector {
		
		private String m_x, m_y, m_z;
		
		public Vector(String x, String y) {
			m_x = x;
			m_y = y;
			m_z = "0";
		}
		
		public Vector(String x, String y, String z) {
			m_x = x;
			m_y = y;
			m_z = z;
		}
		
		public String getX() { 
			return m_x;
		}
		
		public String getY() { 
			return m_y;
		}
		
		public String getZ() { 
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
