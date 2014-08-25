package org.thingswithworth.circuitscan.cv;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

class Finder {
	public void runAll(String pathScene, String pathResult) {
		runAll(pathScene,pathResult,true);
	}

	public void runAll(String pathScene, String pathResult, boolean sample) {
		File imgs = new File("res\\components\\resistor\\positive");
		System.out.println("\nRunning Finder");
		File sceneFile = new File(pathScene);
		Mat img_scene = Highgui.imread(sceneFile.getAbsolutePath(), 0);
		MatOfKeyPoint keypoints_scene  = new MatOfKeyPoint();
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF); //4 = SURF 
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF); //2 = SURF;
		DescriptorMatcher matcher = DescriptorMatcher.create(1); // 1 = FLANNBASED

		detector.detect(img_scene, keypoints_scene);
		Mat descriptor_scene = new Mat() ;
		extractor.compute(img_scene, keypoints_scene, descriptor_scene);
		MatOfDMatch matches = new MatOfDMatch();

		List<Mat> images = new ArrayList<Mat>();
		for(File imgfile:imgs.listFiles()){
			Mat	img_object = Highgui.imread(imgfile.getAbsolutePath(), 0); //0 = CV_LOAD_IMAGE_GRAYSCALE
			if(img_object!=null){
				images.add(img_object);
			}
		}

		List<MatOfKeyPoint> keypoints = new ArrayList<MatOfKeyPoint>();
		detector.detect(images, keypoints);

		List<Mat> descriptors = new ArrayList<Mat>();
		extractor.compute(images, keypoints, descriptors);

		matcher.add(descriptors);
		matcher.match(descriptor_scene, matches);
		List<DMatch> matchesList = matches.toList();
		Double max_dist = 0.0;
		Double min_dist = 100.0;
		double total = 0;
		for(Mat desc:descriptors){
			for(int i = 0; i < desc.rows(); i++){
				Double dist = (double) matchesList.get(i).distance;
				total+=1/dist;
				if(dist < min_dist) min_dist = dist;
				if(dist > max_dist) max_dist = dist;
			}
		}

		System.out.println("-- Max dist : " + max_dist);
		System.out.println("-- Min dist : " + min_dist);    
		//int max=0;
		HashMap<Integer,Integer[]> counts = new HashMap<Integer,Integer[]>();
		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
		MatOfDMatch gm = new MatOfDMatch();
		int numSample = 500;
		while(good_matches.size()<numSample){
			double chance = Math.random()*total;
			double at=0;
			int index = 0;
			for(Mat desc:descriptors){
				if(!counts.containsKey(index))
					counts.put(index, new Integer[desc.rows()]);
				for(int i = 0; i < desc.rows(); i++){
					Double dist = (double) matchesList.get(i).distance;
					at+=(1/dist);
					if(at>chance && dist<min_dist*2.48 ){
						good_matches.addLast(matchesList.get(i));
						if(counts.get(index)[i]==null)
							counts.get(index)[i]=0;
						counts.get(index)[i]++;
						break;
					}
				}
				index++;
				if(at>chance)
					break;
			}
		}
		ArrayList<Integer[]> sorted = new ArrayList<Integer[]>();
		for(Integer intg:counts.keySet()){
			Integer[] arr = counts.get(intg);
			for(int i=0;i<arr.length;i++){
				if(arr[i]!=null)
					sorted.add(new Integer[]{intg,i,arr[i]});
			}
		}
		Collections.sort(sorted, new Comparator<Integer[]>(){
			@Override
			public int compare(Integer[] o1, Integer[] o2) {
				return o2[2]-o1[2];
			}
		});
		for(Integer[] intarr:sorted){
			System.out.println(intarr[2]+" samples at "+intarr[0]+","+intarr[1]);
		}
		gm.fromList(good_matches);

		LinkedList<Point> objList = new LinkedList<Point>();
		LinkedList<KeyPoint> sceneList = new LinkedList<KeyPoint>();
		List<KeyPoint> keypoints_sceneList = keypoints_scene.toList();
		Mat img_matches = new Mat();

		for(DMatch match:good_matches){
			objList.addLast(keypoints.get(match.imgIdx).toList().get(match.trainIdx).pt);
			sceneList.addLast(keypoints_sceneList.get(match.queryIdx));
		}
		MatOfKeyPoint keyMat = new MatOfKeyPoint();
		keyMat.fromList(sceneList);
		Features2d.drawKeypoints(img_scene, keyMat, img_matches);

		/*

		MatOfPoint2f scene = new MatOfPoint2f();
		scene.fromList(sceneList);

		Mat hg = Calib3d.findHomography(obj, scene);

		Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
		Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);

		obj_corners.put(0, 0, new double[] {0,0});
		obj_corners.put(1, 0, new double[] {img_object.cols(),0});
		obj_corners.put(2, 0, new double[] {img_object.cols(),img_object.rows()});
		obj_corners.put(3, 0, new double[] {0,img_object.rows()});

		Core.perspectiveTransform(obj_corners,scene_corners, hg);

		Core.line(img_matches, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 255, 0),4);
		Core.line(img_matches, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),4);
		Core.line(img_matches, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(3,0)), new Scalar(0, 255, 0),4);
		Core.line(img_matches, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);
		 */
		//Sauvegarde du résultat
		System.out.println(String.format("Writing %s", pathResult));
		Highgui.imwrite(pathResult, img_matches);
	}

	public void runOne(String pathObject, String pathScene, String pathResult) {
		System.out.println("\nRunning FindObject");

		Mat img_object = Highgui.imread((new File(pathObject)).getAbsolutePath(), 0); //0 = CV_LOAD_IMAGE_GRAYSCALE
		Mat img_scene = Highgui.imread((new File(pathScene)).getAbsolutePath(), 0);

		FeatureDetector detector = FeatureDetector.create(4); //4 = SURF 
		DescriptorExtractor extractor = DescriptorExtractor.create(2); //2 = SURF;
		DescriptorMatcher matcher = DescriptorMatcher.create(1); // 1 = FLANNBASED

		MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
		MatOfKeyPoint keypoints_scene  = new MatOfKeyPoint();
		Mat descriptor_object = new Mat();
		Mat descriptor_scene = new Mat() ;
		detector.detect(img_object, keypoints_object);
		detector.detect(img_scene, keypoints_scene);
		extractor.compute(img_object, keypoints_object, descriptor_object);
		extractor.compute(img_scene, keypoints_scene, descriptor_scene);

		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(descriptor_object, descriptor_scene, matches);
		List<DMatch> matchesList = matches.toList();

		Double max_dist = 0.0;
		Double min_dist = 100.0;
		for(int i = 0; i < descriptor_object.rows(); i++){
			Double dist = (double) matchesList.get(i).distance;
			if(dist < min_dist) min_dist = dist;
			if(dist > max_dist) max_dist = dist;
		}
		System.out.println("-- Max dist : " + max_dist);
		System.out.println("-- Min dist : " + min_dist);    

		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
		MatOfDMatch gm = new MatOfDMatch();
		for(int i = 0; i < descriptor_object.rows(); i++){
			if(matchesList.get(i).distance < 1.5*min_dist){
				good_matches.addLast(matchesList.get(i));
			}
		}
		gm.fromList(good_matches);

		Mat img_matches = new Mat();
		Features2d.drawMatches(
				img_object,
				keypoints_object, 
				img_scene,
				keypoints_scene, 
				gm, 
				img_matches, 
				new Scalar(255,0,0), 
				new Scalar(0,0,255), 
				new MatOfByte(), 
				2);

		LinkedList<Point> objList = new LinkedList<Point>();
		LinkedList<Point> sceneList = new LinkedList<Point>();

		List<KeyPoint> keypoints_objectList = keypoints_object.toList();
		List<KeyPoint> keypoints_sceneList = keypoints_scene.toList();

		for(int i = 0; i<good_matches.size(); i++){
			objList.addLast(keypoints_objectList.get(good_matches.get(i).queryIdx).pt);
			sceneList.addLast(keypoints_sceneList.get(good_matches.get(i).trainIdx).pt);
		}

		MatOfPoint2f obj = new MatOfPoint2f();
		obj.fromList(objList);

		MatOfPoint2f scene = new MatOfPoint2f();
		scene.fromList(sceneList);

		Mat hg = Calib3d.findHomography(obj, scene);

		Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
		Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);

		obj_corners.put(0, 0, new double[] {0,0});
		obj_corners.put(1, 0, new double[] {img_object.cols(),0});
		obj_corners.put(2, 0, new double[] {img_object.cols(),img_object.rows()});
		obj_corners.put(3, 0, new double[] {0,img_object.rows()});

		Core.perspectiveTransform(obj_corners,scene_corners, hg);

		Core.line(img_matches, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 255, 0),4);
		Core.line(img_matches, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),4);
		Core.line(img_matches, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(3,0)), new Scalar(0, 255, 0),4);
		Core.line(img_matches, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);

		//Sauvegarde du résultat
		System.out.println(String.format("Writing %s", pathResult));
		Highgui.imwrite(pathResult, img_matches);
	} 
}


