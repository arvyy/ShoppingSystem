<template>
	<div>
		<hr>
		<SearchBar v-on:do-search="onSearch" :category="category" :searchtext="searchtext" :categories="categories"></SearchBar>
		<hr>
		<ProductList id="product_list"
			   :products="productPages"
			   @add-to-cart="$emit('add-to-cart', $event)"
			   @set-page="onChangePage" />
	</div>
</template>

<script>
import Vue from 'vue'
import axios from 'axios'
import SearchBar from './SearchBar.vue'
import ProductList from './ProductList.vue'
import LogInForm from './LogInForm.vue'

export default {
	name: 'SearchPage',
	props: ['searchtext', 'page', 'category'],
	data () {
		return {
			productsPerPage: 20,
			productPages: {},
			categories: []
		}
	},
	watch: {
		'$route': function(to, from) {
			this.loadItemsList(this.page);
		}
	},
	methods: {
		getQuery: function() {
			return {
				text: this.searchtext,
				page: this.page,
				category: this.category
			};
		},
		onSearch : function(params) {
			var q = {
				text: params.searchText,
				category: params.category,
				page: 0
			};
			this.$router.push({name: 'Search', query: q});
		},
		onChangePage: function(page) {
			var q = this.getQuery();
			q.page = page;
			this.$router.push({name: 'Search', query: q});
		},
		loadItemsList(page) {
			var t = this;
			axios.get('/api/product/page', {
				params: {
					text: t.searchtext,
					category: t.category,
					size: t.productsPerPage,
					page: page
				}
			}).then(function(response){
				t.productPages = response.data;
				console.log(response.data)
			});
		},
		loadCategories: function() {
			var t = this;
			axios.get('/api/category').then(function(resp) {
				t.categories = resp.data;
			});
		}
	},
	mounted : function() {
		this.loadItemsList(0);
		this.loadCategories();
	},

	components: {
		SearchBar,
		ProductList,
		LogInForm
	}
}


</script>

<style scoped>
#items_list {
	text-align: left;

}

div.main_page_container {

}

</style>
