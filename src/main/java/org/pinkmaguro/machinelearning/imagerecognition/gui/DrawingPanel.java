package org.pinkmaguro.machinelearning.imagerecognition.gui;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.pinkmaguro.machinelearning.imagerecognition.gui.data.GUICommonData;

public class DrawingPanel extends JPanel {

	private GUICommonData commonData;
	private BufferedImage offscreenImage;
	private Graphics offscreen;
	private ImageIcon icon;
	private JLabel draw;
	
	private int panelSizeX, panelSizeY;
	
	public DrawingPanel(GUICommonData commonData) {
		this.commonData = commonData;
		this.panelSizeX = this.commonData.getPanelSizeY();
		this.panelSizeY = this.commonData.getPanelSizeX();
		
		offscreenImage = new BufferedImage(panelSizeX, panelSizeY, 
				BufferedImage.TYPE_BYTE_GRAY );
		offscreen = offscreenImage .createGraphics();
		offscreen.setColor(Color.WHITE);
		offscreen.fillRect(0, 0, panelSizeX, panelSizeY);
        icon = new ImageIcon(offscreenImage);
        draw = new JLabel(icon);
        draw.addMouseMotionListener(new MouseHandler());
        add(draw);
		setPreferredSize(new Dimension(panelSizeX, panelSizeY));
		setBackground(Color.BLUE);
		setVisible(true);
		
	}
	
	
	public BufferedImage getImage() {
		return offscreenImage;
	}
	
	public void clear() {
		offscreen.setColor(Color.WHITE);
		offscreen.fillRect(0, 0, 500, 500);
		repaint();
	}
	
	private class MouseHandler implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent me) {
			if (me.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
				int x = me.getX();
				int y = me.getY();
				offscreen.setColor(Color.BLACK);
				offscreen.fillOval(x, y, commonData.getPenSize(), commonData.getPenSize());
				draw.repaint();
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent me) {
		}
		
	}
}
