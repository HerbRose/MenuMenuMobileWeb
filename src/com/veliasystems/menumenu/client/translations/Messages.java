package com.veliasystems.menumenu.client.translations;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface Messages extends Constants {

	@DefaultStringValue("Please login")
	String pleaseLogin();

	@DefaultStringValue("Login")
	String login();

	@DefaultStringValue("Password")
	String password();

	@DefaultStringValue("OK")
	String ok();

	@DefaultStringValue("Cancel")
	String cancel();

	@DefaultStringValue("City")
	String city();

	@DefaultStringValue("Restaurants")
	String restaurants();

	@DefaultStringValue("Restaurants")
	String appname();

	@DefaultStringValue("Home Page")
	String mainTitle();

	@DefaultStringValue("Add restaurant")
	String addRestaurant();

	@DefaultStringValue("Return")
	String back();

	@DefaultStringValue("Save profile")
	String saveProfile();

	@DefaultStringValue("Remove profile")
	String removeProfile();

	@DefaultStringValue("Save")
	String save();

	@DefaultStringValue("City")
	String cityOne();

	@DefaultStringValue("Name")
	String restaurantName();

	@DefaultStringValue("Address")
	String restaurantAdress();

	@DefaultStringValue("Restaurant saved")
	String restaurantSaved();

	@DefaultStringValue("Edit")
	String edit();

	@DefaultStringValue("Logo")
	String logoPictureTitle();

	@DefaultStringValue("Profile")
	String profilePictureTitle();

	@DefaultStringValue("Menu")
	String maenuPictureTitle();

	@DefaultStringValue("Crop")
	String crop();

	@DefaultStringValue("Main")
	String main();

	@DefaultStringValue("Delete")
	String delete();

	@DefaultStringValue("Upload")
	String upload();

	@DefaultStringValue("Restaurant email")
	String restaurantMail();

	@DefaultStringValue("Phone")
	String restaurantPhone();

	@DefaultStringValue("User mail")
	String userMail();

	@DefaultStringValue("User phone")
	String userPhone();

	@DefaultStringValue("Name")
	String userName();

	@DefaultStringValue("Surname")
	String userSurname();

	@DefaultStringValue("Add user")
	String addUser();

	@DefaultStringValue("Add admin")
	String addAdmin();

	@DefaultStringValue("Add agent")
	String addAgent();

	@DefaultStringValue("Add restaurator")
	String addRestaurator();

	@DefaultStringValue("Input email")
	String inputEmail();

	@DefaultStringValue("Input password")
	String inputPassword();

	@DefaultStringValue("Repeat password")
	String repeatPassword();

	@DefaultStringValue("Logout")
	String logout();

	@DefaultStringValue("Restaurant manager")
	String restauratiunManager();

	@DefaultStringValue("Address")
	String adress();

	@DefaultStringValue("Set visibility")
	String setVisibility();

	@DefaultStringValue("Recipient")
	String addressee();

	@DefaultStringValue("Sender")
	String sender();

	@DefaultStringValue("Message text")
	String messageText();

	@DefaultStringValue("Send email")
	String sendEmail();

	@DefaultStringValue("Send")
	String send();

	@DefaultStringValue("Subject")
	String subject();

	@DefaultStringValue("Do you want send the message without a subject?")
	String confirmationNoSubject();

	@DefaultStringValue("Do you want send the message without content?")
	String confirmationNoMessage();

	@DefaultStringValue("Do you want send the message without a subject and content?")
	String confirmationNoSubjectAndNoMessage();

	@DefaultStringValue("Enter a city")
	String cityPlaceholder();

	@DefaultStringValue("Enter the email")
	String emailPlaceholder();

	@DefaultStringValue("Enter the password")
	String passwordPlaceholder();

	@DefaultStringValue("Repeat the password")
	String repeatPasswordPlaceholder();

	@DefaultStringValue("Enter the restaurant")
	String restaurantPlaceholder();

	@DefaultStringValue("Enter the subject")
	String subjectPlaceholder();

	@DefaultStringValue("Enter the message")
	String messagePlaceholder();

	@DefaultStringValue("An error has occurred, we apologize")
	String error();

	@DefaultStringValue("Connection problem. Please try again later")
	String connectionError();

	@DefaultStringValue("Correct the data")
	String saveRestaurantError();

	@DefaultStringValue("Correct the name and address")
	String emptyBothData();

	@DefaultStringValue("Correct the name")
	String emptyName();

	@DefaultStringValue("Correct the address")
	String emptyAdress();

	@DefaultStringValue("No data")
	String emptyJson();

	@DefaultStringValue("Wrong city")
	String cityError();

	@DefaultStringValue("Restaurant already exist")
	String doubleRestaurant();

	@DefaultStringValue("Add city")
	String addCity();

	@DefaultStringValue("The name must contain City name - City district")
	String wrongCityName();
	
	@DefaultStringValue("Empty emails list")
	String emptyMalList();
	
	@DefaultStringValue("Select e-mail")
	String selectElement();

	@DefaultStringValue("Login and password are required")
	String loginError();
	
	@DefaultStringValue("Login or password is wrong")
	String wrongLoginaData();
	
	@DefaultStringValue("Are you sure want logout?")
	String doYouWantLogout();
	
	@DefaultStringValue("Edit personal data")
	String editData();
	
	@DefaultStringValue("Enter the name")
	String userPlaceholder();
	
	@DefaultStringValue("Enter the surnname")
	String surnamePlaceholder();

	@DefaultStringValue("Enter the old password")
	String inputOldPassword();

	@DefaultStringValue("Data has been changed")
	String changeDataOk();
	
	@DefaultStringValue("Set main empty board")
	String setEmptyBoard();

	@DefaultStringValue("Remove users")
	String removeUsers();
	
	@DefaultStringValue("Upload speed depends on size of chosen image and your Internet connection")
	String largeImageInfo();

	@DefaultStringValue("The city with the same name already exists")
	String cityExistError();

	@DefaultStringValue("The restaurant with the same name and address already exists")
	String restaurantExistError();

	@DefaultStringValue("Check data and try again")
	String wrongDataError();

	@DefaultStringValue("The request was sent. This may take more then 10 minutes. You will get a message on your email when finished")
	String copyRestaurantsInProgress();
	
	@DefaultStringValue("Email has been sent")
	String emailSend();
	
	@DefaultStringValue("Do you want to send copy to you?")
	String isCopyMailToMe();
	
	@DefaultStringValue("Are you sure want to delete this item? Operation can not be undone")
	String areYouSureWantToDelete();
	
	@DefaultStringValue("Go to")
	String goToRestaurant();
	
	@DefaultStringValue("Name")
	String cityName();
	
	@DefaultStringValue("Visibility")
	String visibility();
	
	@DefaultStringValue("Hidden")
	String hidden();
	
	@DefaultStringValue("Visible")
	String visible();
	
	@DefaultStringValue("City manager")
	String cityManager();
	
	@DefaultStringValue("Administration Panel")
	String adminPanel();
	
	@DefaultStringValue("District")
	String district();
	
	@DefaultStringValue("Manager name")
	String bossName();
	
	@DefaultStringValue("Add board")
	String addBoard();

	@DefaultStringValue("Published")  
	String profilePublished();
	
	@DefaultStringValue("Last uploaded photo") 
	String lastUploaded();
	
	@DefaultStringValue("Unpublished") 
	String profileUnPublished();
	
	@DefaultStringValue("Delete image") 
	String deleteImage();
	
	@DefaultStringValue("Remove board") 
	String removeBoard();

	@DefaultStringValue("Given e-mail address is not a valid email address") 
	String wrongEmailAddress();
	
	@DefaultStringValue("Date created") 
	String dateCreated();
	
	@DefaultStringValue("Image type") 	
	String imageType();
	
	@DefaultStringValue("Visibility for tests") 	
	String visibilityForTests();
	
	@DefaultStringValue("District image") 
	String districtImage();
	
	@DefaultStringValue("User is not allowed to edit this restaurant") 
	String userNotAllowed();
	
	@DefaultStringValue("User does not exist") 
	String userNonExist();
	
	@DefaultStringValue("Other restaurant with the same address and name exist in chosen address") 
	String restaurantExist();

	@DefaultStringValue("Users")
	String users();
	
	@DefaultStringValue("Poland") 
	String poland();
	
	@DefaultStringValue("France") 
	String france();

	@DefaultStringValue("Country")
	String country();
	
	@DefaultStringValue("Name") 
	String name();
	
	@DefaultStringValue("Do you want to download mobile application?") 
	String getMobileApp();
	
	@DefaultStringValue("No thanks") 
	String noThanks();
	
	@DefaultStringValue("Download app") 
	String getApp();
	
	@DefaultStringValue("Choose restaurants") 
	String chooseRestaurants();
	
	@DefaultStringValue("Choose")
	String choose();
	
	@DefaultStringValue("Password and repeated password are not the same") 
	String wrongPasswords();
	
	@DefaultStringValue("Empty list with restaurants") 
	String emptyList();
	
	@DefaultStringValue("Invalid phone number") 
	String wrongPhoneNumber();
	
	@DefaultStringValue("Role") 
	String role();
	
	@DefaultStringValue("Empty user type") 
	String emptyType();
	
	@DefaultStringValue("Empty city list") 
	String emptyCityList();
	
	@DefaultStringValue("Empty restaurant list")
	String emptyRestaurantList();

	@DefaultStringValue("User role")
	String userRole();

	@DefaultStringValue("Choose cities")
	String chooseCities();
	
	@DefaultStringValue("Password and repeated password are not the same")
	String passAndRepeatIncorrect();
	

	@DefaultStringValue("Old password missing")
	String oldPasswordMissing();

	@DefaultStringValue("Error")
	String popUpErrorHeader();

	@DefaultStringValue("Confirm")
	String popUpConfirmHeader();

	@DefaultStringValue("Success")
	String popUpSuccessHeader();

	@DefaultStringValue("Warning")
	String popUpWarningHeader();
	
	
	
	@DefaultStringValue("Posting photo means that you accept rules. Do you agree?")
	String publishProfileInfo();
	
	
	@DefaultStringValue("Posting photo means that you accept rules. Do you agree?")
	String publishImageInfo();
	
	
	@DefaultStringValue("Input email for another user to be accessed to this restaurant")
	String inputEmailforUser();
	
	
	@DefaultStringValue("Error while creating new user profile, please contact to administrator")
	String userAddProblem();
	
	
	@DefaultStringValue("Monday")
	String monday();
	
	
	@DefaultStringValue("Tuesday")
	String tuesday();
	
	
	@DefaultStringValue("Wednesday")
	String wednesday();
	
	
	@DefaultStringValue("Thursday")
	String thursday();
	
	
	@DefaultStringValue("Friday")
	String friday();
	
	
	@DefaultStringValue("Saturday")
	String saturday();
	
	
	@DefaultStringValue("Sunday")
	String sunday();
	
	
	@DefaultStringValue("Restaurant Open Hours")
	String restaurantOpenHours();
	
	
	@DefaultStringValue("Open")
	String open();
	
	
	@DefaultStringValue("Closed")
	String closed();

	
	@DefaultStringValue("Input Hours")
	String inputHours();
	
	
	@DefaultStringValue("Missing open hours")
	String missingOpenHours();

	@DefaultStringValue("Unfortunately cookie support appears to be turned off in your browser. You have to turn on cookie support in your browser")
	String noCookieSupportWarning();

	@DefaultStringValue("Browser you are using look like firefox, unfortunately this application doesn't support this kind of web browser. Please use another browser (we recommended browser with gecko engine)")
	String firefoxWarning();

	@DefaultStringValue("Copy city data to:")
	String copyCityDataTo();

	@DefaultStringValue("Copy")
	String copy();

	@DefaultStringValue("Users 2")
	String usersPanel();
	
	@DefaultStringValue("Admin")
	String admin();
	
	@DefaultStringValue("Agent")
	String agent();
	
	@DefaultStringValue("Restaurator")
	String restaurator();
	
	
	
}
