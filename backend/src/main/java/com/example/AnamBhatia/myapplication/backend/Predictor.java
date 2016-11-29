package com.example.AnamBhatia.myapplication.backend;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.WebRequestor;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;


/**
 * Servlet implementation class Predictor
 */
//@WebServlet("/Predictor")
public class Predictor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Predictor() {
        super();
        // TODO Auto-generated constructor stub
    }

	public static List<User> getMyFriends(String token) {
		FacebookClient privateFBClient = new DefaultFacebookClient(token);
		Connection<User> myFriends = privateFBClient.fetchConnection(
				"me/friends", User.class,
				Parameter.with("fields", "first_name, last_name"));
		return myFriends.getData();
	}
    
    static <K,V extends Comparable<? super V>> 
    List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {
	
	List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
	
	Collections.sort(sortedEntries, 
	    new Comparator<Entry<K,V>>() {
	        @Override
	        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
	            return e2.getValue().compareTo(e1.getValue());
	        }
	    }
	);
	
	return sortedEntries;
	}
    
    List<Entry<String,Integer>> individualSortedLikes;
    Map<String,String> idName = new HashMap<>();
    List<List<String>> postLikes = new ArrayList<>();
	List<List<String>> postLikesId = new ArrayList<>();
	List<Date> postCreatedTime = new ArrayList<>();
	List<String> postMessage = new ArrayList<>();
	List<Integer> postTags = new ArrayList<>();
	List<Integer> postComments = new ArrayList<>();
	List<Long> postShares = new ArrayList<>();
	List<String> postType = new ArrayList<>();
	ArrayList<String> friendname=new ArrayList<>();
	ArrayList<String> posts=new ArrayList<>();
	ArrayList<Long> postsdate=new ArrayList<>();
	List<String> postId = new ArrayList<>();
	Map<String,Integer> individualLikes = new LinkedHashMap<String,Integer>();
	int numPosts;
	int numLikes=0;
	int numComments=0;
	int numShares=0;
	int count=0;
	int temp=0;

	int photoLikes=0,statusLikes=0,linkLikes=0,videoLikes=0,photonum=0,statusnum=0,linknum=0,videonum=0,taggedposts=0;
	float likesPerTag=0;
	int totalWords=0,likesWords=0,lastYearLikes=0,lastYearPosts=0,last2YearLikes=0,last2YearPosts=0;
	float meanLikes;
	float likesChangePercentage;
	float averagePhotos;
	float averageStatus;
	float averageLinks;
	float averageVideos;
	float meanTags;
	float likesPerWord;
	String myname;
	
    public void compute(String access){

    	FacebookClient facebookClient = new DefaultFacebookClient(access);
		User user = facebookClient.fetchObject("me", User.class);
		Page page = facebookClient.fetchObject("cocacola", Page.class);

		myname=user.getFirstName()+ " "+ user.getLastName();

		System.out.println("Starting");
		individualLikes.clear();
		idName.clear();
		postLikes.clear();
	 postLikesId.clear();
	 postCreatedTime.clear();
	 postMessage.clear();
	postTags.clear();
    postComments.clear();
		postsdate.clear();
		friendname.clear();
		posts.clear();
	postShares.clear();
	postType.clear();
 postId.clear();

		 numLikes=0;
		 numComments=0;
		 numShares=0;
		 count=0;
		 temp=0;

		 photoLikes=0;statusLikes=0;linkLikes=0;videoLikes=0;photonum=0;statusnum=0;linknum=0;videonum=0;taggedposts=0;
		 likesPerTag=0;
		 totalWords=0;likesWords=0;lastYearLikes=0;lastYearPosts=0;last2YearLikes=0;last2YearPosts=0;
		
		Connection<Post> myFeed = facebookClient.fetchConnection("me/posts", Post.class, Parameter.with("fields", "shares,type,with_tags,message,from,to,likes.summary(true),comments.summary(true)"),Parameter.with("limit", 5000),Parameter.with("likes.limit", 5000),Parameter.with("offset",0),Parameter.with("type","post"));
		List<User> myFriends = getMyFriends(access);


		for (User friend : myFriends) {
			friendname.add(friend.getFirstName()+" "+friend.getLastName());

		}
		for (int i=0;i<friendname.size();i++) {

			System.out.println(friendname.get(i));
		}


		for (List<Post> myFeedConnectionPage : myFeed){
			for (Post post : myFeedConnectionPage){

				long likesTotalCount;
				if(post.getLikes().getData().size()<25){

					likesTotalCount=post.getLikes().getData().size();
				}

				else {
					Post.Likes likes = facebookClient.fetchObject(post.getId() + "/likes", Post.Likes.class,
							Parameter.with("summary", 1), Parameter.with("limit", 500));
					 likesTotalCount = likes.getData().size();
					System.out.println("likes: " + likesTotalCount);
				}

				postsdate.add(likesTotalCount);

				if(post.getMessage()!=null) {
					System.out.println(post.getMessage()+" "+post.getType());
					posts.add(post.getMessage().substring(0, 1).toUpperCase()+ post.getMessage().substring(1)+ " : "+post.getType().toUpperCase());
				}
				else{

					System.out.println(post.getType());
					posts.add(post.getType().toUpperCase());
				}

			if(post!=null)
				if(post.getLikes()!=null)
					if(post.getLikes().getData()!=null)
					{
						//do{

							if((post.getLikes().getData().size()!=-1))
							{
								postLikes.add(new ArrayList<String>());
								postLikesId.add(new ArrayList<String>());
								for(int i=0;i<post.getLikes().getData().size();i++)
								{
									postLikes.get(count).add((post.getLikes().getData().get(i).getName()));
									//System.out.println(post.getLikes().getData().get(i).getName());
									postLikesId.get(count).add((post.getLikes().getData().get(i).getId()));
									if(idName.containsKey((post.getLikes().getData().get(i).getId()))==false)
										idName.put((post.getLikes().getData().get(i).getId()), (post.getLikes().getData().get(i).getName()));
									if(individualLikes.containsKey((post.getLikes().getData().get(i).getId()))){
										individualLikes.put((post.getLikes().getData().get(i).getId()), individualLikes.get((post.getLikes().getData().get(i).getId()))+1);
									}else{
										individualLikes.put((post.getLikes().getData().get(i).getId()), 1);
									}
								}
								postId.add(post.getId());
								postCreatedTime.add((Date) post.getCreatedTime());
								postMessage.add(post.getMessage());
								postTags.add(post.getMessageTags().size()+post.getWithTags().size());
								System.out.println("tags: "+post.getMessageTags().size()+post.getWithTags().size());
								if(post.getComments().getData().size()<25){

									postComments.add(post.getComments().getData().size());
								}
								else
								{
									Post.Comments comm = facebookClient.fetchObject(post.getId() + "/comments", Post.Comments.class,
											Parameter.with("summary", 1), Parameter.with("limit", 500));
									postComments.add(comm.getData().size());

								}
								postShares.add(post.getSharesCount());
								postType.add(post.getType());
								count++;
//								temp++;
//								if(temp<15)
//									System.out.println(post);
							}
						//}while(post.);
					}



		    }
		}
		numPosts=postsdate.size();
		
		
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -1); // to get previous year add -1
		Date lastYear = cal.getTime();
		cal.add(Calendar.YEAR, -1);
		Date last2Year = cal.getTime();

		System.out.print("co:"+postComments.size());
		
		for(int i=0;i<numPosts;i++){
			numLikes+=postsdate.get(i);
			numComments+=postComments.get(i);
			numShares+=postShares.get(i);
//			if(postCreatedTime.get(i).after(last2Year)&&postCreatedTime.get(i).before(lastYear)){
//				last2YearLikes+=postLikes.get(i).size();
//				last2YearPosts+=1;
//			}else if(postCreatedTime.get(i).after(lastYear)){
//				lastYearLikes+=postLikes.get(i).size();
//				lastYearPosts+=1;
//			}
			if(postMessage.get(i)!=null){
//				System.out.println(postMessage.get(i));
//				System.out.println(postMessage.get(i).split(" ").length);
				totalWords+=postMessage.get(i).split(" ").length;
				likesWords+=postLikes.get(i).size();
			}
			if(postTags.get(i)!=0){
				likesPerTag+=(float)postLikes.get(i).size()/postTags.get(i);
				taggedposts+=1;
			}
			if(postType.get(i).equals("photo")){
				photoLikes+=postLikes.get(i).size();
				photonum+=1;
			}else if(postType.get(i).equals("status")){
				statusLikes+=postLikes.get(i).size();
				statusnum+=1;
			}else if(postType.get(i).equals("link")){
				linkLikes+=postLikes.get(i).size();
				linknum+=1;
			}else{
				videoLikes+=postLikes.get(i).size();
				videonum+=1;
			}
		}

		
		individualSortedLikes = entriesSortedByValues(individualLikes);
		
		
		System.out.println("Top 10 Likers are: ");
		for(int i=0;i<10;i++){
			String id = individualSortedLikes.get(i).getKey();
			Integer n = individualSortedLikes.get(i).getValue();
			String name = idName.get(id);
			System.out.println(name+" #likes="+n);
		}
		 meanLikes=(float)numLikes/numPosts;
		 likesChangePercentage=100*(((float)lastYearLikes/lastYearPosts)-(float)last2YearLikes/last2YearPosts)/((float)last2YearLikes/last2YearPosts);
		 averagePhotos=(float)photoLikes/photonum;
		 averageStatus=(float)statusLikes/statusnum;
		 averageLinks=(float)linkLikes/linknum;
		 averageVideos=(float)videoLikes/videonum;
		 meanTags=likesPerTag/taggedposts;
		 likesPerWord=(float)likesWords/totalWords;
		 
		System.out.println("Total number of Likes = "+numLikes);
		System.out.println("Total number of Comments = "+numComments);
		System.out.println("Total number of Posts = "+numPosts);
		System.out.println("Total number of Shares = "+numShares);
		System.out.println("Average number of Likes = "+meanLikes);
		System.out.println("Average number of Comments = "+(float)numComments/numPosts);
		System.out.println("Average number of Shares = "+(float)numShares/numPosts);
		System.out.println("Average likes for photos = "+averagePhotos);
		System.out.println("Average likes for status = "+averageStatus);
		System.out.println("Average likes for links = "+averageLinks);
		System.out.println("Average likes for videos = "+averageVideos);
		System.out.println("Average Likes/Tag for Tagged Posts = "+meanTags);
		System.out.println("Average number of Likes/word = "+likesPerWord);
		System.out.println("Average number of Likes for last year = "+(float)lastYearLikes/lastYearPosts);
		System.out.println("Average number of Likes for last to last year = "+(float)last2YearLikes/last2YearPosts);
		System.out.println("Likes change percentage = "+likesChangePercentage);

		System.out.println("\n\n----------------------------------------\nNow Predicting for Latest 10 posts based on previous 80%\n\n");
		
		int meanLikes2,numLikes2=0,numPosts2=0;
		int last2YearLikes2=0,last2YearPosts2=0,lastYearLikes2=0,lastYearPosts2=0;
		int totalWords2=0,likesWords2=0;
		int likesPerTag2=0,taggedposts2=0;
		int photoLikes2=0,photonum2=0,statusLikes2=0,statusnum2=0,linkLikes2=0,linknum2=0,videoLikes2=0,videonum2=0;
		for(int i=numPosts/5;i<numPosts;i++){
			numLikes2+=postLikes.get(i).size();
			numPosts2+=1;
//			if(postCreatedTime.get(i).after(last2Year)&&postCreatedTime.get(i).before(lastYear)){
//				last2YearLikes2+=postLikes.get(i).size();
//				last2YearPosts2+=1;
//			}else if(postCreatedTime.get(i).after(lastYear)){
//				lastYearLikes2+=postLikes.get(i).size();
//				lastYearPosts2+=1;
//			}
			if(postMessage.get(i)!=null){
//				System.out.println(postMessage.get(i));
//				System.out.println(postMessage.get(i).split(" ").length);
				totalWords2+=postMessage.get(i).split(" ").length;
				likesWords2+=postLikes.get(i).size();
			}
			if(postTags.get(i)!=0){
				likesPerTag2+=(float)postLikes.get(i).size()/postTags.get(i);
				taggedposts2+=1;
			}
			if(postType.get(i).equals("photo")){
				photoLikes2+=postLikes.get(i).size();
				photonum2+=1;
			}else if(postType.get(i).equals("status")){
				statusLikes2+=postLikes.get(i).size();
				statusnum2+=1;
			}else if(postType.get(i).equals("link")){
				linkLikes2+=postLikes.get(i).size();
				linknum2+=1;
			}else{
				videoLikes2+=postLikes.get(i).size();
				videonum2+=1;
			}
		}
		
		 float meanLikes22=(float)numLikes2/numPosts2;
		 float likesChangePercentage2=100*(((float)lastYearLikes2/lastYearPosts2)-(float)last2YearLikes2/last2YearPosts2)/((float)last2YearLikes2/last2YearPosts2);
		 float averagePhotos2=(float)photoLikes2/photonum2;
		 float averageStatus2=(float)statusLikes2/statusnum2;
		 float averageLinks2=(float)linkLikes2/linknum2;
		 float averageVideos2=(float)videoLikes2/videonum2;
		 float meanTags2=likesPerTag2/taggedposts2;
		 float likesPerWord2=(float)likesWords2/totalWords2;
//		 float error=0;
//		 int count=0;
		 System.out.println(likesChangePercentage2);
		 
		for(int i=numPosts/5-1;i>=0;i--){
			int postLength2=0,numTags2=0;
			float prediction;
			count+=1;
			if(postMessage.get(i)!=null)
				postLength2=postMessage.get(i).split(" ").length;
			numTags2=postTags.get(i);
			if(postType.get(i).equals("photo")){
				prediction=(meanLikes22+averagePhotos2+numTags2*meanTags2+likesPerWord2*postLength2+meanLikes22*(1+likesChangePercentage2/100))/5;
				System.out.println("Post id = "+postId.get(i));
				System.out.println("Predicted Likes = "+prediction);
				System.out.println("Actual Likes = "+postLikes.get(i).size());
//				error+=(Math.abs(prediction-(float)postLikes.get(i).size()))/postLikes.get(i).size();
			}else if(postType.get(i).equals("status")){
				prediction=(meanLikes22+averageStatus2+numTags2*meanTags2+likesPerWord2*postLength2+meanLikes22*(1+likesChangePercentage2/100))/5;
				System.out.println("Post id = "+postId.get(i));
				System.out.println("Predicted Likes = "+prediction);
				System.out.println("Actual Likes = "+postLikes.get(i).size());
//				error+=(Math.abs(prediction-(float)postLikes.get(i).size()))/postLikes.get(i).size();
			}else if(postType.get(i).equals("link")){
				prediction=(meanLikes22+averageLinks2+numTags2*meanTags2+likesPerWord2*postLength2+meanLikes22*(1+likesChangePercentage2/100))/5;
				System.out.println("Post id = "+postId.get(i));
				System.out.println("Predicted Likes = "+prediction);
				System.out.println("Actual Likes = "+postLikes.get(i).size());
//				error+=(Math.abs(prediction-(float)postLikes.get(i).size()))/postLikes.get(i).size();
			}else{
				prediction=(meanLikes22+averageVideos2+numTags2*meanTags2+likesPerWord2*postLength2+meanLikes22*(1+likesChangePercentage2/100))/5;
				System.out.println("Post id = "+postId.get(i));
				System.out.println("Predicted Likes = "+prediction);
				System.out.println("Actual Likes = "+postLikes.get(i).size());
//				error+=(Math.abs(prediction-(float)postLikes.get(i).size()))/postLikes.get(i).size();
			}	
		}
//		System.out.println("Average Error = "+error/count);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    void printYolo(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
    	System.out.println("yolo");
    }
    private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
	    String appId = "1015319081843357";
	    String secretKey = "c5fc84cc03920c6a6722df6290163f92";

	    WebRequestor wr = new DefaultWebRequestor();
	    WebRequestor.Response accessTokenResponse = wr.executeGet(
	            "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl
	            + "&client_secret=" + secretKey + "&code=" + code);

	    return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		//doGet(request, response);
		String name = request.getParameter("name");
		response.setContentType("text/plain");
		if(name == null) {
			response.getWriter().println("Please enter a name");
		}
		response.getWriter().println("Hello " + name);
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		// TODO Auto-generated method stub
		String accesstoken=request.getParameter("name");

		if(accesstoken == null) {
			response.getWriter().println("Please enter a name");
		}
		else {

			compute(accesstoken);

		}
//		response.getWriter().append("<a href=\"https://graph.facebook.com/oauth/authorize?client_id=1015319081843357&scope=email,user_posts&redirect_uri=<%=URLEncoder.encode(\"http://localhost:8080/Predict-A-Like/fbAuth\")%>\">Click Here to Login Using Facebook</a>");
//		String code = request.getParameter("code");
//		if(code==null||code.equals("")){
//			System.out.println("Invalid Code");
//		}
//		FacebookClient.AccessToken token = getFacebookUserToken(code, "http://localhost:8080/Predict-A-Like/fbAuth");
//		String accessToken = token.getAccessToken();
//		System.out.println(accessToken);
//		Date expires = (Date) token.getExpires();
//		response.getWriter().append(""
//				+ "<html xmlns='http://www.w3.org/1999/xhtml'> <head> <meta http-equiv='content-type' content='text/html; charset=utf-8' /> <title>Predict-A-Like</title> <meta name='keywords' content='' /> <meta name='description' content='' /> <link href='./style.css' rel='stylesheet' type='text/css' media='screen' /> </head> <body> <div id='header'> <div id='logo'> <h1><a >Predict-A-Like </a></h1> </div> <hr /> <!-- end #logo --> </div> <!-- end #header --> <!-- end #header-wrapper --> <div id='wrapper'> <div id='wrapper-bgtop'> <div id='wrapper-bgbtm'> <div id='page'> <div id='content'> <div class='post'> <h2 class='title'><a href='#'>Calculate Expected Likes</a></h2> <div class='entry'> <textarea name='message' rows='5' cols='50' id='status'> Enter Status </textarea></br><textarea name='tags' rows='5' cols='50' id='numtags'>Enter no. of Tags</textarea> <br> <button type='button' onclick=\"calculate()\">Calculate</button> </div> </div> <div class='post'> <h2 class='title'><a>BFFS</a></h2> <div class='entry'> <ul>");
//		response.getWriter().append("<li><a>Total number of Likes = "+numLikes+"</a></li>");
//		response.getWriter().append("<li><a>Total number of Comments = "+numComments+"</a></li>");
//		response.getWriter().append("<li><a>Total number of Posts = "+numPosts+"</a></li>");
//		response.getWriter().append("<li><a>Total number of Shares = "+numShares+"</a></li>");
//		response.getWriter().append("<li><a>Average number of Likes = "+meanLikes+"</a></li>");
//		response.getWriter().append("<li><a>Average number of Comments = "+(float)numComments/numPosts+"</a></li>");
//		response.getWriter().append("<li><a>Average number of Shares = "+(float)numShares/numPosts+"</a></li>");
//		response.getWriter().append("<li><a>Average likes for photos = "+averagePhotos+"</a></li>");
//		response.getWriter().append("<li><a>Average likes for status = "+averageStatus+"</a></li>");
//		response.getWriter().append("<li><a>Average likes for links = "+averageLinks+"</a></li>");
//		response.getWriter().append("<li><a>Average likes for videos = "+averageVideos+"</a></li>");
//		response.getWriter().append("<li><a>Average Likes/Tag for Tagged Posts = "+meanTags+"</a></li>");
//		response.getWriter().append("<li><a>Average number of Likes/word = "+likesPerWord+"</a></li>");
//		response.getWriter().append("<li><a>Average number of Likes for last year = "+(float)lastYearLikes/lastYearPosts+"</a></li>");
//		response.getWriter().append("<li><a>Average number of Likes for last to last year = "+(float)last2YearLikes/last2YearPosts+"</a></li>");
//		response.getWriter().append("<li><a>Likes change percentage = "+likesChangePercentage+"</a></li>");
//		response.getWriter().append("<ul> </div> </div> </div> <!-- end #content --> <div id='sidebar'> <ul> <li> <h2>About Us</h2> <p>Welcome to Predict-A-Like. We have used sophisticated algorithms to guess the number of likes on your future posts.</p> </li> <li> <h2>Top 10 Likers</h2> <ul> ");
		ArrayList<String> n1=new ArrayList<>();
		ArrayList<Integer> n2=new ArrayList<>();
		for(int i=0;i<100;i++){
			String id = individualSortedLikes.get(i).getKey();
			Integer n = individualSortedLikes.get(i).getValue();
			String name = idName.get(id);
			n1.add(name);
			n2.add(n);
		}

		int likes=numLikes;
		 String nl=new Gson().toJson(likes);
		String nl1=new Gson().toJson(numComments);
		String nl2=new Gson().toJson(numPosts);
		String nl3=new Gson().toJson(numShares);
		String j1= new Gson().toJson(n2);

		String json = new Gson().toJson(n1);
		String post=new Gson().toJson(posts);
		String postdate=new Gson().toJson(postsdate);
		String name=new Gson().toJson(myname);
		String tags=new Gson().toJson(postTags);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(j1);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(post);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(postdate);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(nl);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(nl1);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(nl2);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(nl3);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(name);
		response.getWriter().write("___anambhatia___");
		response.getWriter().write(tags);




//		response.getWriter().append("</ul> </li> </ul> </div> <!-- end #sidebar --> </div> <!-- end #page --> <div style='clear: both;'>&nbsp;</div> </div> </div> </div> <div id='footer-bgcontent'> <div id='footer'> <p>Copyright (c) 2015 Pizza. All rights reserved. </p> </div> </div> <!-- end #footer --> </body> <script type='text/javascript'>"
//				+ "function calculate(){"
//				+ "var meanLikes = "+meanLikes+";"
//				+ "var averagePhotos = "+averagePhotos+";"
//				+ "var averageStatus = "+averageStatus+";"
//				+ "var averageLinks = "+averageLinks+";"
//				+ "var averageVideos = "+averageVideos+";"
//				+ "var meanTags = "+meanTags+";"
//				+ "var numTags = document.getElementById(\"numtags\").value"+";"
//				+ "var likesPerWord = "+likesPerWord+";"
//				+ "var likesChangePercent = "+likesChangePercentage+";"
//				+ "var postLength = document.getElementById(\"status\").value.split(' ').length"+";"
//				+ "var finalPhotos = (meanLikes+averagePhotos+numTags*meanTags+likesPerWord*postLength+meanLikes*(1+likesChangePercent/100))/5"+";"
//				+ "var finalStatus = (meanLikes+averageStatus+numTags*meanTags+likesPerWord*postLength+meanLikes*(1+likesChangePercent/100))/5"+";"
//				+ "var finalLinks = (meanLikes+averageLinks+numTags*meanTags+likesPerWord*postLength+meanLikes*(1+likesChangePercent/100))/5"+";"
//				+ "var finalVideos = (meanLikes+averageVideos+numTags*meanTags+likesPerWord*postLength+meanLikes*(1+likesChangePercent/100))/5"+";"
//				+ "window.alert('Predicted number of likes\\nPhoto: '+finalPhotos+'\\nStatus: '+finalStatus+'\\nLinks: '+finalLinks+'\\nVideos: '+finalVideos);"
//				+ "}"
//				+ "</script> "
//				+ "</html> ");

	}

}
