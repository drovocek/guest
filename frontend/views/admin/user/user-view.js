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

class UserView extends PolymerElement {
    _attachDom(dom) {
        // Do not use a shadow root
        this.appendChild(dom);
    }

    static get template() {
        // language=HTML
        return html`
            <vaadin-vertical-layout style="width: 100%; height: 100%;">
                <div style="flex-grow:1;width:100%;" id="grid-wrapper">
                    <vaadin-grid id="grid"></vaadin-grid>
                </div>
            </vaadin-vertical-layout>
        `;
    }

    static get is() {
        return 'user-view';
    }
}

customElements.define(UserView.is, UserView);

