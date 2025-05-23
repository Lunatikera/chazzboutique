/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author José Iván Vázquez Brambila
 */
public class BotonFlatRound extends JButton{
    
    private int radius = 30;
    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;

    public BotonFlatRound(String texto) {
        setText(texto);
        setFocusPainted(false);
        setBorderPainted(false);
        setBorder(null);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        color=new Color(176, 50, 53);
        colorOver=color.darker();
        colorClick=color.brighter();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(colorOver);
                over = true;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(color);
                over = false;

            }

            @Override
            public void mousePressed(MouseEvent me) {
                setBackground(colorClick);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (over) 
                    setBackground(colorOver);
                 else 
                    setBackground(color);
            }
        });
                        
    }
    
    
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //  Paint Border
        g2.setColor(getBackground());
        //  Border set 2 Pix
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        super.paintComponent(grphcs);
    }
    
}
