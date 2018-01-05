package view.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ia.InfoGenetic;
import mainPkg.Main;

public class DisplayInfoGenetic extends JPanel{
	
	private JLabel numGen;
	private JLabel maxFit;
	private JLabel avgFit;
	private JLabel medianFit;
	private JFreeChart chart;
	private ChartPanel CP;
	private InfoGenetic infoGenetic;
	private SpinnerNumberModel  modelDelay;
	private JSpinner delay;
	private XYSeries seriesAvg;
	private XYSeries seriesMax;
	private JPanel checkboxes;
	private JCheckBox displayMax;
	private JCheckBox displayAvg;
	
	public DisplayInfoGenetic(InfoGenetic infoGenetic) {
		
		this.infoGenetic = infoGenetic;

		this.setBackground(Color.black);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		checkboxes = new JPanel();
		checkboxes.setLayout(new BoxLayout(checkboxes,BoxLayout.LINE_AXIS));
		checkboxes.setAlignmentX(LEFT_ALIGNMENT);
		displayMax = new JCheckBox("Display Max Score");
		displayMax.setSelected(true);
		displayAvg  = new JCheckBox("Display Average Score");
		displayAvg.setSelected(true);
		checkboxes.add(displayAvg);
		checkboxes.add(displayMax);
		
		numGen = new JLabel("Generation "+infoGenetic.getNumberGen());
		numGen.setForeground(Color.white);
		numGen.setFont(new Font("Serif", Font.PLAIN, 32));
		
		maxFit = new JLabel("max fitness : "+infoGenetic.getMaxFit());
		maxFit.setForeground(Color.white);
		
		avgFit = new JLabel("avg fitness : "+infoGenetic.getAvgFit());
		avgFit.setForeground(Color.white);
		
		chart = createChart();
	
		CP = new ChartPanel(chart);
		
		modelDelay = new SpinnerNumberModel(5,-1,30,1);
		delay = new JSpinner(modelDelay);
		delay.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if((int) modelDelay.getValue() != -1) {
					Main.enableView = true;
					Main.delay= (int) modelDelay.getValue();
				}else {
					Main.enableView = false;
				}
				
			}
			
		});
		

		
		this.add(numGen);
		this.add(maxFit);
		this.add(avgFit);
		this.add(checkboxes);
		this.add(CP);
		this.add(delay);
	}

	private JFreeChart createChart() {

		
		seriesAvg = new XYSeries("Avg score");
		seriesMax = new XYSeries("Max score");
		if(this.displayAvg.isSelected()) {
			for (Iterator iterator = infoGenetic.getSerieAvg().iterator(); iterator.hasNext();) {
				Point point = (Point) iterator.next();
				seriesAvg.add(point.getX(),point.getY());
			}			
		}

		if(this.displayMax.isSelected()) {
			for (Iterator iterator = infoGenetic.getSerieMaxFit().iterator(); iterator.hasNext();) {
				Point point = (Point) iterator.next();
				seriesMax.add(point.getX(), point.getY());
			}			
		}


		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(seriesAvg);
		dataset.addSeries(seriesMax);
		// Generate the graph
		JFreeChart chart = ChartFactory.createXYLineChart(
			"Score over Generations", // Title
			"n Generation", // x-axis Label
			"Number of obstacles", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
		);
		return chart;
	}
	public void updateInfo() {
		//Updating label
		

		//Replacement of components in the Panel
		this.remove(delay);
		this.remove(CP);
		chart = createChart();
		CP = new ChartPanel(chart);
		this.add(CP);
		this.add(delay);
		this.revalidate();
		
		numGen.setText("Generation "+infoGenetic.getNumberGen());
		maxFit.setText("max fitness : "+infoGenetic.getMaxFit());
		avgFit.setText("avg fitness : "+infoGenetic.getAvgFit());
	}
}
