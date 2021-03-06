import {PolymerElement} from '@polymer/polymer/polymer-element';
import {html} from '@polymer/polymer/lib/utils/html-tag';

import '@polymer/iron-icon/iron-icon';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-lumo-styles/all-imports';
import '@vaadin/vaadin-icons';

class MeetView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`                
                <vaadin-horizontal-layout theme="spacing-s" class="card">
                    <vaadin-date-picker label="Arrival date" id="arrivalDate"></vaadin-date-picker>
                    <vaadin-combo-box label="Company" id="companyName"></vaadin-combo-box>
                </vaadin-horizontal-layout>
                <vaadin-grid id="grid"></vaadin-grid>
        `;
    }

    static get is() {
        return 'meet-view';
    }
}

customElements.define(MeetView.is, MeetView);

// <!--                                    <iron-icon icon="vaadin:heart"></iron-icon>-->
// <!--                                    <span class="likes">[[item.likes]]</span>-->
// <!--                                    <iron-icon icon="vaadin:comment"></iron-icon>-->
// <!--                                    <span class="comments">[[item.comments]]</span>-->
// <!--                                    <iron-icon icon="vaadin:connect"></iron-icon>-->
// <!--                                    <span class="shares">[[item.shares]]</span>-->


