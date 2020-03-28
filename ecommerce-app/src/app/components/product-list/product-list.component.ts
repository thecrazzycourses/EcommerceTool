import {Component, OnInit} from '@angular/core';
import {Product} from '../../common/product';
import {ProductService} from '../../services/product.service';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

    products: Product[];
    currentCategoryId: number;
    searchMode: boolean;

    constructor(private productService: ProductService, private route: ActivatedRoute) {
    }

    ngOnInit() {

        /**
         * Default Loading of ProductList Component
         */
        this.route.paramMap.subscribe(() => {
            this.listProducts();
        });

    }

    listProducts() {

        this.searchMode = this.route.snapshot.paramMap.has('keyword');
        if (this.searchMode) {
            this.handleSearchProducts();
        } else {
            this.handleListProducts();
        }

    }

    handleListProducts() {

        // check if id param is available
        const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id');

        if (hasCategoryId) {

            // get the id param string and covert to number using + symbol
            this.currentCategoryId = +this.route.snapshot.paramMap.get('id');

        } else {
            // no category id is available default to 1
            this.currentCategoryId = 1;
        }

        this.productService.getProductList(this.currentCategoryId).subscribe(data => {
            this.products = data;
        });
    }

    handleSearchProducts() {

        const keyword: string = this.route.snapshot.paramMap.get('keyword');
        this.productService.searchProducts(keyword).subscribe(data => {
            this.products = data;
        });
    }
}
