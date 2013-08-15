import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PerspectiveTransform;
import javax.media.jai.RenderedOp;
import javax.media.jai.WarpPerspective;
import javax.swing.JFrame;

public class PerspectiveTransformTest {
	private Canvas customCanvas;
	int x = 10, y = 10;

	public Canvas toCanvas() {
		if (customCanvas == null) {
			customCanvas = new Canvas() {
				private static final long serialVersionUID = 932367309486409810L;

				public void paint(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;

					Image after;
					try {
						BufferedImage before = ImageIO.read(getClass()
						   .getResourceAsStream(
						      "/resources/FieldImages/W09/DC-W09-002S_SP.jpg"));

						int wid = before.getWidth();
						int hit = before.getHeight();
						after = new BufferedImage(wid, hit,
						   BufferedImage.TYPE_INT_ARGB);

						g2.drawImage(before, x, y + before.getHeight(), null);
						g2.drawImage(before, x + before.getWidth(),
						   y + before.getHeight(), null);

						g2.rotate(-Math.PI, x + before.getWidth() / 2,
						   y + before.getHeight() / 2);

						// AffineTransform at = new AffineTransform();

						WarpPerspective warp = new WarpPerspective(
						   PerspectiveTransform.getQuadToQuad(x, y,// x1, y1
						      x + wid, y,// x2, y2
						      x, y + hit,// x3, y3
						      x + wid, y + hit,// x4, y5
						      // dest
						      x, y,// x1, y1
						      x + wid, y,// x2, y2
						      x, y + hit,// x3, y3
						      x + wid, y + hit// x4, y5
						   ));

						ParameterBlock pb = new ParameterBlock();

						pb.addSource(before);
						pb.add(warp);
						pb.add(new InterpolationNearest());

						RenderedOp o = JAI.create("warp", pb);
						after = o.getAsBufferedImage();

						// AffineTransformOp scaleOp = new AffineTransformOp(at,
						// AffineTransformOp.TYPE_BILINEAR);
						// after = scaleOp.filter(before, null);

						g2.drawImage(after, x, y, null);
						g2.drawImage(after, x + before.getWidth(), y, null);
						customCanvas.setBounds(x, y, 500, 500);
						customCanvas.setSize(500, 500);
						customCanvas.setPreferredSize(new Dimension(500, 500));

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				public void update(Graphics g) {
					paint(g);
				}
			};
		}

		return customCanvas;
	}

	public static void main(String[] args) {
		PerspectiveTransformTest ptt = new PerspectiveTransformTest();

		JFrame testFrame = new JFrame("Perspective Transformation");
		testFrame.setSize(new Dimension(500, 500));

		testFrame.add(ptt.toCanvas());
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
	}

}
