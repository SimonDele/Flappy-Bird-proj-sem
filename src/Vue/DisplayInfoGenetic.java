package Vue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;

import javax.swing.BoxLayout;
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
import Main.Main;

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
	private XYSeries series;
	
	public DisplayInfoGenetic(InfoGenetic infoGenetic) {
		
		this.infoGenetic = infoGenetic;

		this.setBackground(Color.black);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		numGen = new JLabel("Generation "+infoGenetic.getNumberGen());
		numGen.setForeground(Color.white);
		numGen.setFont(new Font("Serif", Font.PLAIN, 32));
		
		maxFit = new JLabel("max fitness : "+infoGenetic.getMaxFit());
		maxFit.setForeground(Color.white);
		
		medianFit = new JLabel("median fitness : "+infoGenetic.getMedianFit());
		medianFit.setForeground(Color.white);
		avgFit = new JLabel("avg fitness : "+infoGenetic.getAvgFit());
		avgFit.setForeground(Color.white);
		
		chart = createChart();
	
		CP = new ChartPanel(chart);
		
		modelDelay = new SpinnerNumberModel(10,0,30,1);
		delay = new JSpinner(modelDelay);
		delay.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Main.delay= (int) modelDelay.getValue();
			}
			
		});
		this.add(numGen);
		this.add(maxFit);
		this.add(medianFit);
		this.add(avgFit);
		this.add(CP);
		this.add(delay);
	}

	private JFreeChart createChart() {

		
		series = new XYSeries("XYGraph");
		for (Iterator iterator = infoGenetic.getSerieAvg().iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			series.add(point.getX(),point.getY());
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		// Generate the graph
		JFreeChart chart = ChartFactory.createXYLineChart(
			"Average Fitness over Generations", // Title
			"n Generation", // x-axis Label
			"avg Fitness", // y-axis Label
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
		medianFit.setText("median fitness : "+infoGenetic.getMedianFit());	
		avgFit.setText("avg fitness : "+infoGenetic.getAvgFit());
	}
}
