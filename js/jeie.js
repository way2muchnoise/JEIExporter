var data = [];
var tooltipMap;
var lookupMap;
var cat = 0, recipe = 0;
var gitURL = "http://way2muchnoise.github.io/JEIExporter/exports";

function loadDefaultFiles()
{
	$.getJSON(gitURL + "/minecraft_crafting.json", pushToData);
	$.getJSON(gitURL + "/minecraft_brewing.json", pushToData);
	$.getJSON(gitURL + "/minecraft_fuel.json", pushToData);
	$.getJSON(gitURL + "/minecraft_smelting.json", pushToData);
	$.getJSON(gitURL + "/tooltipMap.json", setTooltipMap);
	$.getJSON(gitURL + "/lookupMap.json", setLookupMap);
}

function pushToData(json)
{
	data.push(json);
	changeBackground();
	udpateRecipe();
}

function setTooltipMap(json)
{
	tooltipMap = json;
	udpateRecipe();
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

function scrolling(event) {
	if (event.originalEvent.wheelDelta >= 0) {
		prevRecipe();
	} else {
		nextRecipe();
	}
}

function drawRecipe(recipe) {
	var items = recipe.ingredientItems;
	var renderSpace = $("#renderSpace");
	renderSpace.empty();
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
		}).addClass("itemstack");
		if (tooltipMap && item.stacks[0])
			itemElement.attr('title', tooltipMap[item.stacks[0]]).tooltip({placement: 'left'})
		renderSpace.append(itemElement);
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

$(document).ready(function() {
	$("#files").on("change", readFiles);
	$("#reset").on("click", function(event) { data = [] });
	$("#catLeft").on("click", prevCategory);
	$("#catRight").on("click", nextCategory);
	$("#recipeLeft").on("click", prevRecipe);
	$("#recipeRight").on("click", nextRecipe);
	$(window).on("mousewheel", scrolling);
	loadDefaultFiles();
});