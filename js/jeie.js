var data = [];
var cat = 0, recipe = 0;

function readFiles(event) {
	var files = event.target.files;

	for (var i = files.length - 1; i >= 0; i--) {
		var reader = new FileReader();
		reader.onload = function(event) {
			data.push(JSON.parse(event.target.result));
			udpateRecipe();
		};
		reader.readAsText(files[i]);
	}
}

function nextCategory() {
	if(++cat >= data.length)
		cat = 0;
	if (data.length > 1)
		recipe = 0;
	udpateRecipe();
}

function prevCategory() {
	if(--cat < 0)
		cat = data.length - 1;
	if (data.length > 1)
		recipe = 0;
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
		var itemElement = $("<div></div>").css({
			width: item.w*2,
			height: item.h*2,
			top: item.y*2,
			left: item.x*2,
			margin: item.p*2
		}).addClass("itemstack");
		renderSpace.append(itemElement);
	}
}

$(document).ready(function() {
	$("#files").on("change", readFiles);
	$("#reset").on("click", function(event) { data = [] });
	$("#catLeft").on("click", prevCategory);
	$("#catRight").on("click", nextCategory);
	$("#recipeLeft").on("click", prevRecipe);
	$("#recipeRight").on("click", nextRecipe);
	$(window).on("mousewheel", scrolling);
});