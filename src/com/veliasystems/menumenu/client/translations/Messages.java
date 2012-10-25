package com.veliasystems.menumenu.client.translations;

import com.google.gwt.i18n.client.Constants;

public interface Messages extends Constants {

	@DefaultStringValue("Please login")
	String pleaseLogin();

	@DefaultStringValue("Login:")
	String login();

	@DefaultStringValue("Password:")
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

	@DefaultStringValue("Back")
	String back();

	@DefaultStringValue("Save profile")
	String saveProfile();

	@DefaultStringValue("Remove profile")
	String removeProfile();

	@DefaultStringValue("Save")
	String save();

	@DefaultStringValue("City")
	String cityOne();

	@DefaultStringValue("Restaurant name")
	String restaurantName();

	@DefaultStringValue("Restaurant address")
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

	@DefaultStringValue("Restaurant phone")
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

	@DefaultStringValue("Restauratiun manager")
	String restauratiunManager();

	@DefaultStringValue("Address")
	String adress();

	@DefaultStringValue("Set visibility")
	String setVisibility();

	@DefaultStringValue("Addressee")
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

	@DefaultStringValue("Do you want to send a message without a subject?")
	String confirmationNoSubject();

	@DefaultStringValue("Do you want to send a message without content?")
	String confirmationNoMessage();

	@DefaultStringValue("Do you want to send a message without a subject and content?")
	String confirmationNoSubjectAndNoMessage();

	@DefaultStringValue("Enter a city")
	String cityPlaceholder();

	@DefaultStringValue("Enter a email")
	String emailPlaceholder();

	@DefaultStringValue("Enter a password")
	String passwordPlaceholder();

	@DefaultStringValue("Repeat a passwrd")
	String repeatPasswordPlaceholder();

	@DefaultStringValue("Enetr a restaurant")
	String restaurantPlaceholder();

	@DefaultStringValue("Enetr a subject")
	String subjectPlaceholder();

	@DefaultStringValue("Enetr a message")
	String messagePlaceholder();

	@DefaultStringValue("There was an error, we apologize")
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

	@DefaultStringValue("The name must contain '-'")
	String wrongCityName();
	
	@DefaultStringValue("Empty emails list")
	String emptyMalList();

	@DefaultStringValue("Empty emails list")
	String emptyMalList();

}
