package com.webapp.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.webapp.controller.GroceryListController;
import com.webapp.service.GroceryListService;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { GroceryListController.class, GroceryListService.class })
public class TestSpring {
	private static WebClient webClient;
	private static HtmlPage page;
	private static String item1 = RandomStringUtils.random(10, true, true),
			item2 = RandomStringUtils.random(10, true, true);
	private static int q1 = (int) (Math.random() * 100 + 10), p1 = (int) (Math.random() * 100 + 10),
			q2 = (int) (Math.random() * 100 + 10), p2 = (int) (Math.random() * 100 + 10);

	@Test
	public void test01_invalidLogin() {
		try {
			webClient = new WebClient();
			page = webClient.getPage("http://localhost:8000/");
			HtmlInput inp = (HtmlInput) page.getByXPath("//input[@name=\"username\"]").get(0);
			inp.setValueAttribute("fresco");
			inp = (HtmlInput) page.getByXPath("//input[@name=\"password\"]").get(0);
			inp.setValueAttribute("pla");
			inp = (HtmlSubmitInput) page.getByXPath("//input[@name=\"submit\"]").get(0);
			page = inp.click();
			assertEquals(page.asText().replaceAll("[\\t\\n ]", "").toLowerCase(),
					"LoginPageYourloginattemptwasnotsuccessful,tryagain.Reason:BadcredentialsLoginwithUsernameandPasswordUser:Password:Login"
							.toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test02_validLogin() {
		try {
			page = webClient.getPage("http://localhost:8000/");
			HtmlInput inp = (HtmlInput) page.getByXPath("//input[@name=\"username\"]").get(0);
			inp.setValueAttribute("fresco");
			inp = (HtmlInput) page.getByXPath("//input[@name=\"password\"]").get(0);
			inp.setValueAttribute("play");
			inp = (HtmlSubmitInput) page.getByXPath("//input[@name=\"submit\"]").get(0);
			page = inp.click();
			assert (page.asText().toLowerCase()
					.contains("Grocery List Application\nFresco Play\nHome\nGrocery List\nLogout".toLowerCase()));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test03_navigationMenu() {
		try {
			HtmlAnchor playA = page.getAnchorByHref("http://play.fresco.me");
			HtmlAnchor homeA = page.getAnchorByHref("/");
			HtmlAnchor groceriesA = page.getAnchorByHref("/list-groceries");
			HtmlAnchor logoutA = page.getAnchorByHref("/logout");
			assert (playA != null && homeA != null && groceriesA != null && logoutA != null);
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test04_emptyDataInGroceryListPage() {
		try {
			HtmlAnchor groceriesA = page.getAnchorByHref("/list-groceries");
			page = groceriesA.click();
			assert (page.asText().replaceAll("[\\t ]", "").toLowerCase().contains(
					"GroceryListApplication\nFrescoPlay\nHome\nGroceryList\nLogout\nGroceries\nItemQuantityPriceperunitTotalPrice\nAdd"
							.toLowerCase()));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test05_addingNewGroceryItem() {
		try {
			HtmlAnchor addBtn = page.getAnchorByHref("/add-item");
			page = addBtn.click();
			HtmlInput inp = (HtmlInput) page.getByXPath("//input[@name=\"item\"]").get(0);
			inp.setValueAttribute(item1);
			inp = (HtmlInput) page.getByXPath("//input[@name=\"quantity\"]").get(0);
			inp.setValueAttribute(String.valueOf(q1));
			inp = (HtmlInput) page.getByXPath("//input[@name=\"pricePerUnit\"]").get(0);
			inp.setValueAttribute(String.valueOf(p1));
			inp = (HtmlInput) page.getByXPath("//input[@name=\"totalPrice\"]").get(0);
			page.executeJavaScript("$('#quantity').keyup();");
			assertEquals(String.valueOf(Integer.parseInt(inp.getValueAttribute())), String.valueOf(q1 * p1));
			HtmlButton btn = (HtmlButton) page.getElementsByTagName("button").get(0);
			page = btn.click();
			assert (page.asText().replaceAll("[\\t .0]", "")
					.contains((item1 + "" + q1 + "" + p1 + "" + q1 * p1 + "EditDelete").replaceAll("0", "")));
			addBtn = page.getAnchorByHref("/add-item");
			page = addBtn.click();
			inp = (HtmlInput) page.getByXPath("//input[@name=\"item\"]").get(0);
			inp.setValueAttribute(item2);
			inp = (HtmlInput) page.getByXPath("//input[@name=\"quantity\"]").get(0);
			inp.setValueAttribute(String.valueOf(q2));
			inp = (HtmlInput) page.getByXPath("//input[@name=\"pricePerUnit\"]").get(0);
			inp.setValueAttribute(String.valueOf(p2));
			btn = (HtmlButton) page.getElementsByTagName("button").get(0);
			page = btn.click();
			assert (page.asText().replaceAll("[\\t .0]", "").contains((item1 + "" + q1 + "" + p1 + "" + q1 * p1
					+ "EditDelete\n" + item2 + "" + q2 + "" + p2 + "" + q2 * p2 + "EditDelete").replaceAll("0", "")));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test06_deletingGroceryItem() {
		try {
			HtmlAnchor delBtn = page.getAnchorByText("Delete");
			page = delBtn.click();
			assert (!page.asText().replaceAll("[\\t .0]", "")
					.contains((item1 + "" + q1 + "" + p1 + "" + q1 * p1 + "EditDelete").replaceAll("0", "")));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test07_editingGroceryItem() {
		try {
			HtmlAnchor editBtn = page.getAnchorByText("Edit");
			page = editBtn.click();
			HtmlInput inp = (HtmlInput) page.getByXPath("//input[@name=\"item\"]").get(0);
			inp.setValueAttribute(item1);
			HtmlButton btn = (HtmlButton) page.getElementsByTagName("button").get(0);
			page = btn.click();
			assert (page.asText().replaceAll("[\\t .0]", "")
					.contains((item1 + "" + q2 + "" + p2 + "" + q2 * p2 + "EditDelete").replaceAll("0", "")));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test08_accessingAdminEndpointShouldThrow403() {
		try {
			boolean didThrow = false;
			try {
				page = webClient.getPage("http://localhost:8000/admin/groceries/");
			} catch (FailingHttpStatusCodeException e) {
				if (e.getMessage().contains("403 Forbidden"))
					didThrow = true;
			}
			assert (didThrow);
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test09_logoutAndLoginAsAdminShouldHaveEmptyGroceryDataAndShouldAccessAdminEndpoint() {
		try {
			page = webClient.getPage("http://localhost:8000/");
			HtmlAnchor logoutBtn = page.getAnchorByText("Logout");
			page = logoutBtn.click();
			HtmlInput inp = (HtmlInput) page.getByXPath("//input[@name=\"username\"]").get(0);
			inp.setValueAttribute("admin");
			inp = (HtmlInput) page.getByXPath("//input[@name=\"password\"]").get(0);
			inp.setValueAttribute("admin");
			inp = (HtmlSubmitInput) page.getByXPath("//input[@name=\"submit\"]").get(0);
			page = inp.click();
			HtmlAnchor groceriesA = page.getAnchorByHref("/list-groceries");
			page = groceriesA.click();
			assert (page.asText().replaceAll("[\\t ]", "").toLowerCase()
					.contains("ItemQuantityPriceperunitTotalPrice\nAdd".toLowerCase()));
			UnexpectedPage response = webClient.getPage("http://localhost:8000/admin/groceries/");
			String resString = IOUtils.toString(response.getInputStream());
			assert (resString.contains(item1));
			page = webClient.getPage("http://localhost:8000/");
			logoutBtn = page.getAnchorByText("Logout");
			page = logoutBtn.click();
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test10_loginAsUserAndDeleteRemainingDataFinallyShouldBeEmpty() {
		try {
			HtmlInput inp = (HtmlInput) page.getByXPath("//input[@name=\"username\"]").get(0);
			inp.setValueAttribute("fresco");
			inp = (HtmlInput) page.getByXPath("//input[@name=\"password\"]").get(0);
			inp.setValueAttribute("play");
			inp = (HtmlSubmitInput) page.getByXPath("//input[@name=\"submit\"]").get(0);
			page = inp.click();
			HtmlAnchor groceriesA = page.getAnchorByHref("/list-groceries");
			page = groceriesA.click();
			HtmlAnchor delBtn = page.getAnchorByText("Delete");
			page = delBtn.click();
			assert (page.asText().replaceAll("[\\t ]", "").toLowerCase().contains(
					"GroceryListApplication\nFrescoPlay\nHome\nGroceryList\nLogout\nGroceries\nItemQuantityPriceperunitTotalPrice\nAdd"
							.toLowerCase()));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Autowired
	GroceryListController groceryController;

	@Test
	@WithMockUser(username = "admin")
	public void test11_checkingSecurityWithValidUser() {
		try {
			System.out.println(groceryController.showGroceries(new ModelMap()));
		} catch (Exception e) {
			assert (false);
		}
		assert (true);
	}

	@Test
	public void test12_checkingSecurityWithInvalidUserShouldThrowError() {
		boolean flag = false;
		try {
			groceryController.showGroceries(new ModelMap());
		} catch (Exception e) {
			flag = true;
		}
		assert (flag);
	}

	@Test
	public void test13_checkingLogging() {
		try {
			File file = new File("/tmp/test-output");
			assert (FileUtils.readFileToString(file).contains("INFO"));
			assert (FileUtils.readFileToString(file).contains("TRACE"));
			assert (FileUtils.readFileToString(file).contains("DEBUG"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

}
