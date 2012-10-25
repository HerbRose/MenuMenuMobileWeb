package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.GWT;
import com.veliasystems.menumenu.client.translations.Messages;

public class Customization {
	
	private static Messages translated = GWT.create(Messages.class);

	
	public static final String CITY = translated.city();
	public static final String RESTAURANTS = "Restauracje";  
	public static final String APPNAME ="Restauracje";
	public static final String MAINTITLE = "Strona główna";
	public static final String ADDRESTAURANT = "Dodaj restaurację";
	public static final String BACK = "Wstecz";
	public static final String SAVEPROFILE = "Zapisz profil";
	public static final String REMOVEPROFILE = "Usuń profil";
	public static final String SAVERESTAURANT = "Zapisz";
	public static final String CITYONE = "Miasto";
	public static final String RESTAURANTNAME = "Nazwa restauracji";
	public static final String RESTAURANTADRESS = "Adres restauracji";
	public static final String RESTAURANTSAVED = "Restauracja zapisana";
	public static final String SAVERRESTAURANTERROR = "Popraw dane";
	public static final String LOADERROR = "Wystąpił błąd, przepraszamy";
	public static final String EDIT = "Edytuj";
	public static final String EMPTYBOTHDATA = "Popraw nazwę i adres";
	public static final String EMPTYNAME = "Popraw nazwę";
	public static final String EMPTYADRESS = "Popraw adres";
	public static final String LOGO_PICTURE_TITLE = "Logo";
	public static final String PROFILE_PICTURE_TITLE = "Profile";
	public static final String MAENU_PICTURE_TITLE = "Menu";
	public static final String CROP = "Przytnij";
	public static final String SET_AS_MAIN = "Główne";
	public static final String DELETE_IMAGE = "Usuń";
	public static final String UPLOAD = "Wgraj restauracje";
	public static final String EMPTY_JSON = "Puste dane";
	public static final String CITY_ERROR = "Błedne miasto";
	public static final String DOUBLE_RESTAURANT = "Restauracja istnieje";
	public static final String ADD_CITY = "Dodaj miasto";
	public static final String SAVE = "Zapisz";
	public static final String WRONG_CITY_NAME = "Nazwa musi zawierać '-' ";
	public static final String RESTAURANT_MAIL = "E-mail restauracji";
	public static final String RESTAURANT_PHONE = "Telefon do restauracji";
	public static final String USER_MAIL = "E-mail do kontaktu";
	public static final String USER_PHONE = "Telefon do kontaktu";
	public static final String USER_NAME = "Imię";
	public static final String USER_SURNAME = "Nazwisko";
	public static final String ADD_USER = "Dodaj użytkownika";
	public static final String ADD_ADMIN = "Dodaj administratora";
	public static final String ADD_AGENT = "Dodaj przedstawiciela";
	public static final String ADD_RESTAURATOR = "Dodaj restauratora";
	public static final String IS_ADMIN = "Administrator";
	public static final String INPUT_EMAIL= "Podaj e-mail";
	public static final String INPUT_PASSWORD = "Ustal hasło";
	public static final String REPEAT_PASSWORD = "Powtórz hasło";

	public static final String LOGOUT = "Logout";
	public static final String RESTAURATIUN_MANAGER = "Zarządzanie restauracjami";
	public static final String ADRESS = "Adres";
	public static final String SET_VISIBILITY = "Ustaw widoczność restauracji";
	public static final String CONNECTION_ERROR = "Connection problem. Please try again later";
	public static final String ADDRESSEE = "Adresat";
	public static final String SENDER = "Nadawca";
	public static final String MESSAGE_TEXT = "Text wiadomości";
	public static final String SEND_EMAIL = "Wyślij e-mail";
	public static final String SEND = "Wyślij";
	public static final String SUBJECT = "Temat";
	public static final String CONFIRMATION_NO_SUBJECT = "Czy chcesz wysłać wiadomość bez tematu?";
	public static final String CONFIRMATION_NO_MESSAGE = "Czy chcesz wysłać wiadomość bez treści?";
	public static final String CONFIRMATION_NO_SUBJECT_AND_NO_MESSAGE = "Czy chesz wysłać wiadomość bez tematu i treści?";
	public static final String CITY_PLACEHOLDER = "Wprowadź miasto";
	public static final String EMAIL_PLACEHOLDER = "Wprowadź e-mail";
	public static final String PASSWORD_PLACEHOLDER = "Wprowadź hasło";
	public static final String REPEAT_PASSWORD_PLACEHOLDER = "Powtórz hasło";
	public static final String RESTAURANT_PLACEHOLDER = "Wprowadź restaurację";
	public static final String SUBJECT_PLACEHOLDER = "Wprowadź temat";
	public static final String MESSAGE_PLACEHOLDER = "Wprowadź treść";
};
