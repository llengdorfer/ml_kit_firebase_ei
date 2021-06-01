# Exercise with ML Firebase 
After finishing both the deployment and the first task, you can now move on to a more difficult excercise. 

## Cloning the Project
The first step in this exercise is to again clone a remote project.
File -> New -> Project from Version Control... -> https://github.com/llengdorfer/ml_kit_firebase_ei/tree/face_vision
## Code Explanation
With the FirebaseVisionFaceDetectorOptions object you can select the options you want the Face Detector to function on. 
Optionally you can play around with the settings and choose different constants to influence the detection.
```
FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();
```

The chooseImage() method takes you to the camera to take a picture.
```
Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
```
The actual Face Detector itself is present in the analyzeFaces() method. 
```
        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        /* Access the recognized face with faces.get(0).getSmilingProbability()
                                           faces.get(0).getLeftEyeOpenProbability() and faces.get(0).getRightEyeOpenProbability())
                                        */
                                    }
                                });
```
The only thing you really need to understand here is that faces.get(0) gets you the first face detected and the methods mentioned in the comment contained in the onSuccess block give you the String value of the probability mentioned in the method name. 
If you run your code at this point it won't output anything. The output will be the final step of the exercise.
### Implementation
Now read through the following options and choose one of the two for implementation. 
Both of these options provide you a method, where you provide a String and it gets showed to the user. 
You can combine multiple Strings with a simple "+" and output static ones with "text". An example for the combination of Strings could be:
```
faces.get(0).getSmilingProbability() + " is a float"
```
#### Toast
  Where the position of the word "String" is marks the place where you should write the String combination you want to print out. 
  ```
  Toast.makeText(getApplicationContext(), "String",Toast.LENGTH_SHORT).show();  
  ```
  You can read through the following link for more information and context.
  https://developer.android.com/reference/android/widget/Toast
#### TextView
  There already is a TextView object provided in the project. Its reference is accessible by ``textView``.
  To change the Text of a TextView you use the setText() method and again where the text "String" is you can specify the things you want to print out.
  ```
  textView.setText("String");
  ```
  You can read through the following link for more information and context.
  https://developer.android.com/reference/android/widget/TextView?hl=en
