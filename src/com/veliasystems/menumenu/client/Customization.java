
package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.GWT;
import com.veliasystems.menumenu.client.translations.Messages;
/**
 * 
 *class with all translations
 *
 */
public class Customization {
	
	private static Messages translated = GWT.create(Messages.class); 

	public static final long NEW_CITY = 1;//used when need copy data for city
	public static final long DO_NOTHING = 0;//used when need copy data for city

	public static final String DELETE_IMAGE = translated.deleteImage();
	public static final String CITY = translated.city();
	public static final String RESTAURANTS = translated.restaurants();  
	public static final String APPNAME = translated.appname();
	public static final String MAINTITLE = translated.mainTitle();
	public static final String ADDRESTAURANT = translated.addRestaurant();
	public static final String BACK = translated.back();
	public static final String SAVEPROFILE =  translated.saveProfile();
	public static final String REMOVEPROFILE = translated.removeProfile();
	public static final String SAVERESTAURANT = translated.save();
	public static final String CITYONE = translated.cityOne();
	public static final String RESTAURANTNAME = translated.restaurantName();
	public static final String RESTAURANTADRESS = translated.restaurantAdress();
	public static final String RESTAURANTSAVED = translated.restaurantSaved();
	public static final String SAVERRESTAURANTERROR = translated.saveRestaurantError();

	public static final String EDIT = translated.edit();
	public static final String EMPTYBOTHDATA = translated.emptyBothData();
	public static final String EMPTYNAME = translated.emptyName();
	public static final String EMPTYADRESS = translated.emptyAdress();
	public static final String LOGO_PICTURE_TITLE = translated.logoPictureTitle();
	public static final String PROFILE_PICTURE_TITLE = translated.profilePictureTitle();
	public static final String MAENU_PICTURE_TITLE = translated.maenuPictureTitle();
	public static final String CROP = translated.crop();
	public static final String SET_AS_MAIN = translated.main();
	public static final String DELETE = translated.delete();
	public static final String UPLOAD = translated.upload();
	public static final String EMPTY_JSON = translated.emptyJson();
	public static final String CITY_ERROR = translated.cityError();
	public static final String DOUBLE_RESTAURANT = translated.doubleRestaurant();
	public static final String ADD_CITY = translated.addCity();
	public static final String SAVE = translated.save();
	public static final String WRONG_CITY_NAME = translated.wrongCityName();
	public static final String RESTAURANT_MAIL = translated.restaurantMail();
	public static final String RESTAURANT_PHONE = translated.restaurantPhone();
	public static final String USER_MAIL = translated.userMail();
	public static final String USER_PHONE = translated.userPhone();
	public static final String USER_NAME = translated.userName();
	public static final String USER_SURNAME = translated.userSurname();
	public static final String ADD_USER = translated.addUser();
	public static final String ADD_ADMIN = translated.addAdmin();
	public static final String ADD_AGENT = translated.addAgent();
	public static final String ADD_RESTAURATOR = translated.addRestaurator();
	public static final String EDIT_DATA = translated.editData();
	public static final String INPUT_EMAIL= translated.inputEmail();
	public static final String INPUT_PASSWORD = translated.inputPassword();
	public static final String REPEAT_PASSWORD = translated.repeatPassword();
	public static final String LOGOUT = translated.logout();
	public static final String RESTAURATIUN_MANAGER = translated.restauratiunManager();
	public static final String ADRESS = translated.adress();
	public static final String SET_VISIBILITY = translated.setVisibility();
	public static final String CONNECTION_ERROR = translated.connectionError();
	public static final String ADDRESSEE = translated.addressee();
	public static final String SENDER = translated.sender();
	public static final String MESSAGE_TEXT = translated.messageText();
	public static final String SEND_EMAIL = translated.sendEmail();
	public static final String SEND = translated.send();
	public static final String SUBJECT = translated.subject();
	public static final String CONFIRMATION_NO_SUBJECT = translated.confirmationNoSubject();
	public static final String CONFIRMATION_NO_MESSAGE = translated.confirmationNoMessage();
	public static final String CONFIRMATION_NO_SUBJECT_AND_NO_MESSAGE = translated.confirmationNoSubjectAndNoMessage();
	public static final String CITY_PLACEHOLDER = translated.cityPlaceholder();
	public static final String EMAIL_PLACEHOLDER = translated.emailPlaceholder();
	public static final String PASSWORD_PLACEHOLDER = translated.passwordPlaceholder();
	public static final String REPEAT_PASSWORD_PLACEHOLDER = translated.repeatPasswordPlaceholder();
	public static final String RESTAURANT_PLACEHOLDER = translated.restaurantPlaceholder();
	public static final String SUBJECT_PLACEHOLDER = translated.subjectPlaceholder();
	public static final String MESSAGE_PLACEHOLDER = translated.messagePlaceholder();
	public static final String USER_NAME_PLACEHOLDER = translated.userPlaceholder();
	public static final String USER_SURNNAME_PLACEHOLDER = translated.surnamePlaceholder();
	public static final String INPUT_OLD_PASSWORD = translated.inputOldPassword();
	public static final String EMPTY_MAIL_LIST = translated.emptyMalList();
	public static final String SELECT_ELEMENT = translated.selectElement();
	public static final String LOGIN_ERROR = translated.loginError();
	public static final String WRONG_LOGIN_DATA = translated.wrongLoginaData();
	public static final String ARE_YOU_SURE_WANT_LOGOUT = translated.doYouWantLogout();
	public static final String CHANGE_OK = translated.changeDataOk();
	public static final String SET_DEFAULT_EMPTY_PROFIL = translated.setEmptyBoard();
	public static final String REMOVE_USERS = translated.removeUsers();
	public static final String INFO = translated.largeImageInfo();


	public static final String ADMIN_PANEL = translated.adminPanel();


	public static final String DISTRICT = translated.district();


	public static final String ADD_BOARD = translated.addBoard();


	public static final  String LAST_UPLOADED = translated.lastUploaded();


	public static final String EMAIL_SEND = translated.emailSend();
	public static final String CITY_EXIST_ERROR = translated.cityExistError();
	public static final String RESTAURANT_EXIST_ERROR = translated.restaurantExistError();


	public static final String WRONG_DATA_ERROR = translated.wrongDataError();
	public static final String COPY_RESTAURANTS_IN_PROGRESS = translated.copyRestaurantsInProgress();


	public static final String IS_TO_ME_COPY = translated.isCopyMailToMe();


	public static final String ARE_YOU_SURE_WANT_DELETE = translated.areYouSureWantToDelete();


	public static final String GO_TO_RESTAURANT = translated.goToRestaurant();


	public static final String CITY_MANAGER = translated.cityManager();


	public static final String CITY_NAME = translated.cityName();


	public static final String VISIBILITY = translated.visibility();


	public static final String VISIBLE = translated.visible();


	public static final String HIDDEN = translated.hidden();


	public static final String LOGIN = translated.login();


	public static final String OK = translated.ok();


	public static final String CANCEL = translated.cancel();


	public static final String PASSWORD = translated.password();


	public static final String BOSS_LABEL = translated.bossName();


	public static final String PROFILE_PUBLISHED = translated.profilePublished();
	
	public static final String PROFILE_UNPUBLISHED = translated.profileUnPublished();

	public static final String REMOVE_BOARD = translated.removeBoard();
	public static final String WRONG_EMAIL_ADDRESS = translated.wrongEmailAddress();

	public static final String ERROR = translated.error();

	public static final String DATE_CREATED = translated.dateCreated();

	public static final String IMAGE_TYPE = translated.imageType();
	
	public static final String VISIBILITY_FOR_TESTS = translated.visibilityForTests();

	public static final String DISTRICT_IMAGE = translated.districtImage();

	public static final String USER_NOT_ALLOWED = translated.userNotAllowed();
	
	public static final String USER_DONT_EXIST = translated.userNonExist();
	
	public static final String RESTAURANT_EXIST_IN_OTHER_CITY = translated.restaurantExist();

	public static final String USERS = translated.users();
	
	
	public static final String POLAND = translated.poland();
	
	public static final String FRANCE = translated.france();
	
	public static final String COUNTRY = translated.country();
	
	public static final String NAME = translated.name();
	
	public static final String GET_MOBILE_APP = translated.getMobileApp();
	
	public static final String NO_THANKS = translated.noThanks();
	
	public static final String GET_APP = translated.getApp();
	
	public static final String CHOOSE_RESTAURANTS = translated.chooseRestaurants();
	public static final String CHOOSE_CITIES = translated.chooseCities();
	
	public static final String CHOOSE = translated.choose();
	
	public static final String WRONG_PASSWORDS =  translated.wrongPasswords();
	
	public static final String EMPTY_LIST = translated.emptyList();
	
	public static final String WRONG_PHONE_NUMBER = translated.wrongPhoneNumber();

	public static final String USER_ROLE = translated.userRole();

	public static final String ROLE = translated.role();
	
	public static final String EMPTY_TYPE = translated.emptyType();
	
	public static final String EMPTY_CITY_LIST = translated.emptyCityList();
	
	public static final String EMPTY_RESTAURANT_LIST = translated.emptyRestaurantList();
	
	public static final String PASSWORD_AND_REPEAT_ARE_NOT_THE_SAME = translated.passAndRepeatIncorrect();
	
	public static final String OLD_PASSWORD_MISSING = translated.oldPasswordMissing();

	public static final String POP_UP_ERROR_HEADER = translated.popUpErrorHeader();

	public static final String POP_UP_CONFIRM_HEADER = translated.popUpConfirmHeader();

	public static final String POP_UP_SUCCESS_HEADER = translated.popUpSuccessHeader();

	public static final String POP_UP_WARNING_HEADER = translated.popUpWarningHeader();
	
	public static final String PUBLISH_PFOFILE_INFO = translated.publishProfileInfo();
	
	public static final String PUBLISH_IMAGE_INFO = translated.publishImageInfo();
	
	public static final String INPUT_EMAIL_FOR_USER = translated.inputEmailforUser();
	
	public static final String ERROR_WHILE_CREATE_NEW_USER = translated.userAddProblem();
	
	public static final String MONDAY = translated.monday();
	
	public static final String TUESDAY = translated.tuesday();
	
	public static final String WEDNESDAY = translated.wednesday();
	
	public static final String THURSDAY = translated.thursday();
	
	public static final String FRIDAY = translated.friday();
	
	public static final String SATURDAY = translated.saturday();
	
	public static final String SUNDAY = translated.sunday();
	
	public static final String RESTAURANT_OPEN_HOURS = translated.restaurantOpenHours();
	
	public static final String OPEN = translated.open();
	
	public static final String CLOSED = translated.closed();
	
	public static final String INPUT_HOURS = translated.inputHours();
	
	public static final String MISSING_OPEN_HOURS = translated.missingOpenHours();
	
	public static final String NO_COOKIE_SUPPORT_WARNING = translated.noCookieSupportWarning();


	public static final String FIREFOX_WARNING = translated.firefoxWarning();


	public static final String COPY_CITY_DATA_TO = translated.copyCityDataTo();
	
	public static final String UPLOAD_SCREEN_PANEL = translated.upload();


	public static final String COPY = translated.copy();
	
	public static final String USERS_PANEL = translated.usersPanel();
	
	public static final String ADMIN = translated.admin();
	
	public static final String AGENT = translated.agent();

	public static final String RESTAURATOR = translated.restaurator();
}
  
  
