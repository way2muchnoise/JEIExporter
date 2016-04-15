var data = [];
var mainData = [];
var tooltipMap;
var lookupMap;
var recipeLinks
var cat = 0, recipe = 0, itemPage = 0,
	itemsPerRow = 6, itemsPerCol = 8,
	itemsPerPage = itemsPerRow * itemsPerCol, totalPages;
var gitURL = "http://way2muchnoise.github.io/JEIExporter/exports";

function loadDefaultFiles()
{
	$.getJSON(gitURL + "/minecraft_crafting.json", pushToData);
	$.getJSON(gitURL + "/minecraft_brewing.json", pushToData);
	$.getJSON(gitURL + "/minecraft_smelting.json", pushToData);
	$.getJSON(gitURL + "/minecraft_fuel.json", pushToData);

	$.getJSON(gitURL + "/tconstruct_alloy.json", pushToData);
	$.getJSON(gitURL + "/tconstruct_casting_table.json", pushToData);
	$.getJSON(gitURL + "/tconstruct_smeltery.json", pushToData);
	$.getJSON(gitURL + "/thaumcraft_crucible.json", pushToData);
	$.getJSON(gitURL + "/thaumcraft_infernal.json", pushToData);
	$.getJSON(gitURL + "/thaumcraft_arcane.json", pushToData);
	$.getJSON(gitURL + "/thaumcraft_crucible.json", pushToData);
	$.getJSON(gitURL + "/thaumcraft_infusion.json", pushToData);

	$.getJSON(gitURL + "/tooltipMap.json", setTooltipMap);
	$.getJSON(gitURL + "/lookupMap.json", setLookupMap);
}


function pushToData(json)
{
	mainData.push(json);
	data = mainData;
	changeBackground();
	udpateRecipe();
}

function setTooltipMap(json)
{
	tooltipMap = json;
	udpateRecipe();
	totalPages = Math.ceil(Object.keys(tooltipMap).length / itemsPerPage);
	drawItemlist();
}

function setLookupMap(json)
{
	lookupMap = json;
	udpateRecipe();
}

function readFiles(event) {
	var files = event.target.files;

	for (var i = files.length - 1; i >= 0; i--) {
		var reader = new FileReader();
		reader.onload = function(event) {
			pushToData(JSON.parse(event.target.result));
		};
		if (files[i].name == "tooltipMap.json") {
			var tooltipReader = new FileReader();
			tooltipReader.onload = function(event) {
				setTooltipMap(JSON.parse(event.target.result));;
			};
			tooltipReader.readAsText(files[i])
		} else if (files[i].name == "lookupMap.json") {
			var lookupReader = new FileReader();
			lookupReader.onload = function(event) {
				setLookupMap(JSON.parse(event.target.result));;
			};
			lookupReader.readAsText(files[i])
		}
		else reader.readAsText(files[i]);
	}
}

function nextCategory() {
	if(++cat >= data.length)
		cat = 0;
	if (data.length > 1)
		recipe = 0;
	changeBackground();
	udpateRecipe();
}

function prevCategory() {
	if(--cat < 0)
		cat = data.length - 1;
	if (data.length > 1)
		recipe = 0;
	changeBackground();
	udpateRecipe();
}

function nextRecipe() {
	if(++recipe >= data[cat].recipes.length)
		recipe = 0;
	udpateRecipe();
}

function prevRecipe() {
	if(--recipe < 0)
		recipe = data[cat].recipes.length - 1;
	udpateRecipe();
}

function udpateRecipe() {
	var toRender = data[cat].recipes[recipe];
	$("#cat").html(data[cat].category);
	$("#recipe").html((recipe+1) + "/" + data[cat].recipes.length);
	drawRecipe(data[cat].recipes[recipe]);
}

function recipeScrolling(event) {
	if (event.originalEvent.wheelDelta >= 0) {
		prevRecipe();
	} else {
		nextRecipe();
	}
}

function listScrolling(event) {
	if (event.originalEvent.wheelDelta >= 0) {
		prevList();
	} else {
		nextList();
	}
}

function drawRecipe(recipe) {
	var renderSpace = $("#renderSpace");
	renderSpace.empty();

	var items = recipe.ingredientItems;
	for (var i = items.length - 1; i >= 0; i--) {
		var item = items[i];
		var image = "";
		if (item.stacks[0])
			image = "url(items/" + item.stacks[0].replace(/:/g, "_") + ".png)";
		var padding = item.p;
		var itemElement = $("<div></div>").css({
			width: item.w*2,
			height: item.h*2,
			top: item.y*2-padding,
			left: item.x*2-padding,
			margin: padding*2,
			'background-image': image
		}).addClass("itemstack").attr({cycle: 0, id: "itemElement" + i});
		if (tooltipMap && item.stacks[0])
			itemElement.attr('title', tooltipMap[item.stacks[0]]).tooltip({placement: 'left'});
		renderSpace.append(itemElement);
		if ($("#itemElement" + i).is(":hover")) itemElement.tooltip('show');
	}

	var fluids = recipe.ingredientFluids;
	for (var i = fluids.length - 1; i >= 0; i--) {
		var fluid = fluids[i];
		var image = "";
		if (fluid.fluids[0])
			image = "url(fluids/" + fluid.fluids[0].replace(/:/g, "_") + ".png)";
		var padding = fluid.p;
		var fluidElement = $("<div></div>").css({
			width: fluid.w*2,
			height: fluid.h*2,
			top: fluid.y*2-padding,
			left: fluid.x*2-padding,
			margin: padding*2,
			'background-image': image
		}).addClass("fluidstack").attr({cycle: 0, id: "fluidElement" + i});
		if (tooltipMap && fluid.fluids[0])
			fluidElement.attr('title', tooltipMap[fluid.fluids[0]]).tooltip({placement: 'left'});
		renderSpace.append(fluidElement);
		if ($("#fluidElement" + i).is(":hover")) fluidElement.tooltip('show');
	}
}

function cycle()
{
	cycleItems();
	cycleFluids();
}

function cycleItems() {
	var items = data[cat].recipes[recipe].ingredientItems;
	for (var i = items.length - 1; i >= 0; i--) {
		var item = items[i];
		var itemElement = $("#itemElement" + i);
		var image = "";
		var ii = itemElement.attr('cycle');
		if (++ii >= item.stacks.length) ii = 0;
		if (item.stacks[ii])
			image = "url(items/" + item.stacks[ii].replace(/:/g, "_") + ".png)";
		itemElement.css('background-image', image).attr('cycle', ii);
		if (tooltipMap && item.stacks[ii])
			itemElement.tooltip('hide')
				.attr('data-original-title', tooltipMap[item.stacks[ii]])
				.tooltip('fixTitle');
		if (itemElement.is(":hover")) itemElement.tooltip('show');
	}
}

function cycleFluids() {
	var fluids = data[cat].recipes[recipe].ingredientFluids;
	for (var i = fluids.length - 1; i >= 0; i--) {
		var fluid = fluids[i];
		var fluidElement = $("#fluidElement" + i);
		var image = "";
		var ii = fluidElement.attr('cycle');
		if (++ii >= fluid.fluids.length) ii = 0;
		if (fluid.fluids[ii])
			image = "url(fluids/" + fluid.fluids[ii].replace(/:/g, "_") + ".png)";
		fluidElement.css('background-image', image).attr('cycle', ii);
		if (tooltipMap && fluid.fluids[ii])
			fluidElement.tooltip('hide')
				.attr('data-original-title', tooltipMap[fluid.fluids[ii]])
				.tooltip('fixTitle');
		if (fluidElement.is(":hover")) fluidElement.tooltip('show');
	}
}

function changeBackground()
{
	var bg = data[cat].bg;
	var image = "";
	if (bg.tex)
		image = "url(bg/" + bg.tex.replace(/:/g, "_") + ".png)";
	$("#renderSpace").css({
		width: bg.w*2,
		height: bg.h*2,
		'background-image': image
	});
}

function nextList() {
	if(++itemPage >= totalPages)
		itemPage = 0;
	drawItemlist();
}

function prevList() {
	if(--itemPage < 0)
		itemPage = totalPages-1;
	drawItemlist();
}

function drawItemlist() {
	var offsetX = 9, offsetY = 32, x = 0, y = 0;
	var size = 18;
	var padding = 1;
	$("#list").html((itemPage+1) + "/" + (totalPages));
	$("#itemlistRenderSpace").empty();
	var skipped = 0;
	for (var item in tooltipMap)
	{
		if (skipped++ < itemPage * itemsPerPage) continue;
		var url, cssClass;
		if (/^fluid:/.test(item))
		{
			url = "fluids/" + item.replace(/:/g, "_") + ".png";
			cssClass = "fluidstack";
		} else
		{
			url = "items/" + item.replace(/:/g, "_") + ".png";
			cssClass = "itemstack";
		}
		var image = "url(" + url + ")";
		var listElement = $("<div></div>").css({
			width: size*2,
			height: size*2,
			top: y*size*2-padding+offsetY,
			left: x*size*2-padding+offsetX,
			margin: padding*2,
			'background-image': image
		}).addClass(cssClass).attr('id', "listedItem" + x + y)
		.attr('title', tooltipMap[item]).tooltip({placement: 'left'});
		$("#itemlistRenderSpace").append(listElement);
		if ($("#listedItem" + x + y).is(":hover")) listElement.tooltip('show');
		if (++x >= itemsPerRow)
		{
			x = 0;
			if (++y >= itemsPerCol) break;
		}
	}
}

function clearAll()
{
	data = [];
	lookupMap = [];
	tooltipMap = [];
	$("#renderSpace").empty();
	$("#renderSpace").css('background-image', "none");
	$("#cat").html("Category");
	$("#recipe").html("Recipe");
}

$(document).ready(function() {
	$("#files").on("change", readFiles);
	$("#reset").on("click", clearAll);
	$("#catLeft").on("click", prevCategory);
	$("#catRight").on("click", nextCategory);
	$("#recipeLeft").on("click", prevRecipe);
	$("#recipeRight").on("click", nextRecipe);
	$("#listLeft").on("click", prevList);
	$("#listRight").on("click", nextList);
	$("#grayBox").on("mousewheel", recipeScrolling);
	$(".itemlist").on("mousewheel", listScrolling);
	loadDefaultFiles();
	setInterval(cycle, 1300);
});