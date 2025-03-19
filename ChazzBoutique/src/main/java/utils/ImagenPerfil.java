/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author carli
 */
public class ImagenPerfil extends JComponent{
    private Icon imagen;
    private int tamanoBorde = 0;
    private Color colorBorde = new Color(60, 60, 60);

    // Getters and Setters
    public Icon getImagen() {
        return imagen;
    }

    public void setImagen(Icon imagen) {
        this.imagen = imagen;
        repaint(); // Repaint the component when the image is updated
    }

    public int getTamanoBorde() {
        return tamanoBorde;
    }

    public void setTamanoBorde(int tamanoBorde) {
        this.tamanoBorde = tamanoBorde;
        repaint(); // Repaint when the border size changes
    }

    public Color getColorBorde() {
        return colorBorde;
    }

    public void setColorBorde(Color colorBorde) {
        this.colorBorde = colorBorde;
        repaint(); // Repaint when the border color changes
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ensure proper background rendering

        if (imagen != null) {
            int ancho = getWidth();
            int altura = getHeight();
            int diametro = Math.min(ancho, altura);

            // Create circular mask
            BufferedImage mascara = new BufferedImage(diametro, diametro, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = mascara.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fillOval(0, 0, diametro, diametro);
            g2d.dispose();

            // Apply mask to image
            BufferedImage imagenCircular = new BufferedImage(diametro, diametro, BufferedImage.TYPE_INT_ARGB);
            g2d = imagenCircular.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(toImage(imagen), 0, 0, diametro, diametro, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
            g2d.drawImage(mascara, 0, 0, null);
            g2d.dispose();

            // Draw the circular image
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = (ancho - diametro) / 2;
            int y = (altura - diametro) / 2;
            g2.drawImage(imagenCircular, x, y, null);

            // Draw the border
            if (tamanoBorde > 0) {
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(tamanoBorde));
                g2.drawOval(x + tamanoBorde / 2, y + tamanoBorde / 2, diametro - tamanoBorde, diametro - tamanoBorde);
            }
        }
    }

    private Image toImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }
        // Create a buffered image for non-ImageIcon icons
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return bufferedImage;
    }
}



