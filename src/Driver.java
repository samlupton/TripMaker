import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.BasicStroke;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.util.Random;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
import org.openstreetmap.gui.jmapviewer.*;

public class Driver extends JFrame implements ActionListener {
	private static final long serialVersionUID = 8566982503262984362L;
	private JMapViewer mapViewer;
	private JComboBox<String> comboBox;
	private JCheckBox checkBox;
	private static JButton button;
	private JPanel compenentJpanel;
	private JPanel mainPanel;
	private int index = 0;
	private int colorValue = 0;
	private int h = 0;
	private int i = 2; 
	private Image image;
	private static final String DEST_DIR = "/Users/samuellupton/Desktop/eclipse-workspace/project5-moo20222/";
	private static JFileChooser fileChooser = new JFileChooser();
	private Random rand = new Random();

	// index of the current trip point

	public Driver() throws FileNotFoundException, IOException {
		setTitle("Project 5 - Samuel Evans-Lupton"); // set the title of the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton addButton = new JButton("Add File");
		addButton.addActionListener(this);

		String[] items = { "Animated Time", "15", "30", "60", "90" };
		comboBox = new JComboBox<>(items);
		checkBox = new JCheckBox("Include Stops");
		button = new JButton("Play");
		compenentJpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		compenentJpanel.add(comboBox);
		compenentJpanel.add(checkBox);
		compenentJpanel.add(button);
		compenentJpanel.add(addButton);
		compenentJpanel.setSize(new Dimension(50, 50));
		compenentJpanel.setBackground(Color.WHITE);
		mapViewer = new JMapViewer();
		mapViewer.setZoom(1);
		mapViewer.setDisplayPosition(new Coordinate(36.0, -99.0), 5);

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {

					drawPolygon();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		mapViewer.setMapMarkerVisible(true);
		mapViewer.setZoomControlsVisible(false);
		mapViewer.setTileSource(new OsmTileSource.TransportMap());
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(compenentJpanel, BorderLayout.NORTH);
		mainPanel.add(mapViewer, BorderLayout.SOUTH);
		mainPanel.setSize(new Dimension(100, 100));

		getContentPane().add(mainPanel);

		setSize(1000, 465);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// Create a file chooser
		
		fileChooser = new JFileChooser(DEST_DIR);
		fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only gpx files", "gpx");

        fileChooser.addChoosableFileFilter(restrict);

		int result = fileChooser.showOpenDialog(this);

		// If the user selects a file, add it
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				new Driver();
				
				Convert.convertFile(selectedFile.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("File selected: " + selectedFile.getName());

			File destFile = new File(DEST_DIR + selectedFile.getName());
			try {
				Files.copy(selectedFile.toPath(), destFile.toPath());
				System.out.println("File saved to: " + destFile.getPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				TripPoint.readFile(selectedFile.getName());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static String getFile() {
		return fileChooser.toString();
	}

	public static int getSelectedValue(JComboBox<String> comboBox) {
		return Integer.parseInt(comboBox.getSelectedItem().toString());
	}

	public ArrayList<TripPoint> getChosenTrip() throws FileNotFoundException, IOException {
		if (checkBox.isSelected()) {
			TripPoint.h2StopDetection();
			return TripPoint.getTrip();
		} else {
			TripPoint.h1StopDetection();
			return TripPoint.getMovingTrip();
		}
	}

	public void drawPolygon() throws FileNotFoundException, IOException {
		try {
			TripPoint.readFile("triplog.csv");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ArrayList<TripPoint> tripPoints = getChosenTrip();
		List<Coordinate> points = new ArrayList<Coordinate>();

		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (index < tripPoints.size()) {
					TripPoint point = tripPoints.get(index);
					double lat = point.getLat();
					double lon = point.getLon();
					points.add(new Coordinate(lat, lon));
					MapPolygonImpl line = new MapPolygonImpl(points.get(0), points.get(0), points.get(0));
					mapViewer.addMapPolygon(line);
//					if (i - 0 < 0) {
					
					if (i > tripPoints.size()) {
						i = tripPoints.size() - 3;
						
					}
						
//						System.out.println(TripPoint.avgSpeed(tripPoints.get(i), tripPoints.get(i - 1)));
						

//					}
					
					if (points.size() - 3 > 0) {
						line = new MapPolygonImpl(points.get(points.size() - 1), points.get(points.size() - 2),
								points.get(points.size() - 3));
					} else {
						line = new MapPolygonImpl(points.get(points.size() - 1), points.get(points.size() - 1),
								points.get(points.size() - 1));
					}


					try {
						// Load the image from a file
						File file = new File("raccoon.png");
						image = ImageIO.read(file);
					} catch (IOException e) {
						System.out.println("Error loading image.");
					}

					IconMarker marker = new IconMarker(points.get(index), 1, image);
					mapViewer.removeAllMapMarkers();
					mapViewer.addMapMarker(marker);
					mapViewer.addMapPolygon(line);

					line.setBackColor(null);
					line.setColor(Color.RED);
					line.setStroke(new BasicStroke(2f));

//					if (colorValue < 255 && h == 0) {
//						line.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
//						colorValue = colorValue + 3;
//					}
//					if (colorValue >= 255) {
//						colorValue = 0;
//					}
					
					int RRatio = (int) (Math.round(TripPoint.avgSpeed(tripPoints.get(i-1), tripPoints.get(i - 2))) / 125.0 * 200); 
				
					int GRatio = RRatio / 255; 
//					int GRatio =  RRatio - 255;
					if (GRatio < 0) {
						GRatio = 0;
						
					}
					
					if (RRatio > 255) {
						RRatio = 255;
						
					}
					if (GRatio > 255) {
						GRatio = 255;
					}

					System.out.println(RRatio);
				
					line.setColor(new Color(RRatio, 255 - RRatio, 0));

					index++;
					i++;
				} else {
					((Timer) evt.getSource()).stop();
				}
			}
		};
		Timer timer = new Timer(getSelectedValue(comboBox) * 1000 / tripPoints.size(), taskPerformer);
		timer.start();
	}
	
	

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		new Driver();
	}
}