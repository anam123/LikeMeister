# Like Prediction Algorithm

Our prediction algorithm takes into account the following 6 variables and then normalizes the result to get an idea of the prediction :
Previous data ( Averages of different types of data )

Change in data with time ( % average increase/decrease)

Type of data ( images, profile pictures, statuses, links , videos )

Weight assigned to a particular word. ( Calculated using already available data )

Number of Tagged Friends 

Length/Size of data

Algorithm Used:
//the calculation step.

//the weight function strips the data of trivial words and assigns weight according to weighted likes for each word. If word not present in data, we assign weight= total likes/ total words=likesperword

            if(profile==false && image==true && video==false && lnk==false)
            {Double finale= (1.6*imagelikes+1.6*((photolikechange+1)*(imagelikes))+0.4*averagelikes+0.4*likesperweight*size+(weight(status1).sum+increasepertagphoto*tags+statuslikes)/6.0;
            answer=Math.ceil(finale);
            }
            else if(profile==false && image==false && video==true && lnk==false)
            {
            Double finale= (1.6*videolikes+1.6*((videolikechange+1)*(videolikes))+0.4*averagelikes+0.4*likesperword*size+(weight(status1).sum)+increasepertagvideo*tags+statuslikes)/6.0;
            answer=Math.ceil(finale);

            }
            else if(profile==false && image==false && video==false && lnk==true)
            {

            Double finale= (1.4*linklikes+1.6*((linklikechange+1)*(linklikes))+0.4*averagelikes+0.6*likesperword*size+(weight(status1).sum)+increasepertaglink*tags+statuslikes)/6.0;
            answer=Math.ceil(finale);

            }
            else if(profile==true && image==true && video==false && lnk==false)
            {
            Double finale= (1.8*profilelikes+1.8*((photolikechange+1)*(profilelikes))+0.4*increasepertagimage*tags+0.2*likesperword*size+(weight(status1).sum))/4.0;
            answer=Math.ceil(finale);

            }
            else
            {

            Double finale= (averagelikes+0.6*likesperword*size+(weight(status1).sum)+increasepertagstatus*tags+statuslikes+1.4*((statuslikechange+1)*(statuslikes)))/5.0;
            answer=Math.ceil(finale);
            }
