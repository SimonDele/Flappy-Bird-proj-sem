package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ia.InfoGenetic;

public class DisplayInfoGenetic extends JPanel{
	
	private JLabel numGen;
	private JLabel maxFit;
	private JLabel medianFit;
	private JFreeChart chart;
	private ChartPanel CP;
	private InfoGenetic infoGenetic;
	
	public DisplayInfoGenetic(InfoGenetic infoGenetic) {
		
		this.infoGenetic = infoGenetic;
		
		//this.setSize(new Dimension(1000, 500));
		this.setBackground(Color.black);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		numGen = new JLabel("Generation n°"+infoGenetic.getNumberGen());
		numGen.setForeground(Color.white);
		numGen.setFont(new Font("Serif", Font.PLAIN, 32));
		
		maxFit = new JLabel("max fitness : "+infoGenetic.getMaxFit());
		maxFit.setForeground(Color.white);
		
		medianFit = new JLabel("median fitness : "+infoGenetic.getMedianFit());
		medianFit.setForeground(Color.white);
		
		chart = createChart();
	
		CP = new ChartPanel(chart);
		this.add(numGen);
		this.add(maxFit);
		this.add(medianFit);
		this.add(CP);
	}

	private JFreeChart createChart() {
		XYSeries series = new XYSeries("XYGraph");
		for (Iterator iterator = infoGenetic.getSerieFit().iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			series.add(point.getX(),point.getY());
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		// Generate the graph
		JFreeChart chart = ChartFactory.createXYLineChart(
			"Median Fitness over Generations", // Title
			"n° Generation", // x-axis Label
			"median Fitness", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
		);
		return chart;
	}
}
