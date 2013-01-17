package group150.helper;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;

import java.net.URI;
import java.net.URL;

import java.security.CodeSource;

import java.util.ArrayList;
import java.util.Random;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import group150.controller.GameManager;

public class GameImage implements FilenameFilter {
	public GameImage(String imageLoc, String imgName) {
		this.imgName = imgName;
		this.imageLoc = imageLoc;
		
		try {
			String[] imageNames;
		
			CodeSource source = GameImage.class.getProtectionDomain().getCodeSource();
			URL sourceLoc = source.getLocation();
			
			if (sourceLoc.toString().endsWith(".jar")) {
				ArrayList<String> list = new ArrayList<String>();
				
				ZipInputStream zipStream = new ZipInputStream(sourceLoc.openStream());
				
				ZipEntry entry = null;
				while ((entry = zipStream.getNextEntry()) != null) {
					File entryFile = new File(entry.getName());
					
					if (this.accept(entryFile.getParentFile(), entryFile.getName())) {
						list.add(entryFile.getName());
					}
				}
				imageNames = list.toArray(new String[list.size()]);
			}
			else {
				File imageDir = new File(sourceLoc.toURI().resolve(imageLoc.toString()));
				imageNames = imageDir.list(this);
			}
			
			images = new BufferedImage[imageNames.length];
			frameCnt = new int[imageNames.length];
			frameStep = new int[imageNames.length];
			
			for (int i = 0; i < imageNames.length; i++) {
				String spriteImg = imageNames[i];
				
				InputStream stream = GameImage.class.getResourceAsStream(String.format("/%s/%s", imageLoc, spriteImg));
				this.images[i] = ImageIO.read(stream);
			
				Pattern p = Pattern.compile("^[a-zA-Z0-9]*(_\\d+)?(_\\d+)?(_[A-Za-z]+)?\\.png$");
				Matcher m = p.matcher(spriteImg);
			
				if (m.find()) {
					if (m.group(1) != null) {
						String frameCntStr = m.group(1).substring(1);
						this.frameCnt[i] = Integer.parseInt(frameCntStr);
					}
					else {
						this.frameCnt[i] = 1;
					}
				
					if (m.group(2) != null) {
						String frameStepStr = m.group(2).substring(1);
						this.frameStep[i] = Integer.parseInt(frameStepStr);
					}
					else {
						this.frameStep[i] = 5;
					}
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String imgName;
	private String imageLoc;
	
	private BufferedImage[] images;
	private int[] frameCnt;
	private int[] frameStep;
	
	public int getNumberOfImages() {
		return this.images.length;
	}
	
	public BufferedImage getImage(int n) {
		return this.images[n];
	}
	
	public int getFrameCnt(int n) {
		return this.frameCnt[n];
	}
	
	public int getFrameStep(int n) {
		return this.frameStep[n];
	}
	
	public boolean accept(File dir, String name) {
		String skinName = new File(this.imageLoc).getParentFile().getName();
	
		if (dir == null) return false;
		
		File parentDir = dir.getParentFile();
		if (parentDir == null) return false;
		
		String pattern = String.format("^%s(_\\d+)?(_\\d+)?(_[A-Za-z])?\\.png$", this.imgName);

		return parentDir.getName().equals(skinName) && name.matches(pattern);
	}
}