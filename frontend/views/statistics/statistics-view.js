import {PolymerElement} from '@polymer/polymer/polymer-element';
import {html} from '@polymer/polymer/lib/utils/html-tag';
import '@vaadin/vaadin-split-layout';
import '@vaadin/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-column';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';

class StatisticsView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`
<!--            <div id="grid-wrapper">-->
                <vaadin-grid
                        theme="compact"
                        id="grid">
                </vaadin-grid>
<!--            </div id="grid-wrapper">-->
        `;
    }

    static get is() {
        return 'statistics-view';
    }
}

customElements.define(StatisticsView.is, StatisticsView);