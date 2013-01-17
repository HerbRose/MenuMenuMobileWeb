package com.veliasystems.menumenu.client.translations;

import com.google.gwt.i18n.client.Constants;

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
	
	@DefaultStringValue("Boss name")
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
	
	@DefaultStringValue("Visibility for tests") 	//not translated yet
	String visibilityForTests();
	
	@DefaultStringValue("District image")  //not translated yet
	String districtImage();
	
	@DefaultStringValue("User is not allowed to edit this restaurant")
	String userNotAllowed();
	
	@DefaultStringValue("User does not exist")
	String userNonExist();
	
	@DefaultStringValue("Other restaurant with th same address and name exist in chosen address")
	String restaurantExist();

	@DefaultStringValue("Users")
	String users();

}
