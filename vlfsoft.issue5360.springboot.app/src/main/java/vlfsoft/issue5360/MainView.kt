package vlfsoft.issue5360

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.github.mvysny.karibudsl.v10.label
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.server.AbstractStreamResource
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@Suppress("unused")
@Route
class MainView : VerticalLayout() {

    init {

        label("issue5360")

        listOf(" ", "-", "+")
                .map { "test${it}file.txt" }
                .forEach {
                    println("File '$it' exists: ${File(it).exists()}")
                    anchorToDownloadWithButton(File(it).toStreamResource(), buttonText = it)
                }

    }

}

// Anchor

fun (@VaadinDsl HasComponents).anchor(href: AbstractStreamResource, anchorText: String? = null, block: (@VaadinDsl Anchor).() -> Unit = {}) =
        init(Anchor(href, anchorText), block)

// Vaadin 10 Let user download a file https://vaadin.com/forum/thread/17010389
// To extend Html.kt
fun (@VaadinDsl HasComponents).anchorToDownload(href: AbstractStreamResource, anchorText: String? = null, block: (@VaadinDsl Anchor).() -> Unit = {}) =
        anchor(href, anchorText) {
            element.setAttribute("download", true)
            block()
        }

/**
 * [buttonIcon] VaadinIcon.DOWNLOAD.create()
 */
fun (@VaadinDsl HasComponents).anchorToDownloadWithButton(href: AbstractStreamResource, buttonIcon: Icon? = VaadinIcon.DOWNLOAD.create(), buttonText: String? = null, anchorText: String? = null, block: (@VaadinDsl Anchor).() -> Unit = {}): Button {
    var downloadButton: Button? = null
    anchorToDownload(href, anchorText) {
        downloadButton = button(buttonText, buttonIcon) {
        }
        block()
    }
    return downloadButton!!
}

fun String.createResource(getStream: () -> InputStream) = StreamResource(this, InputStreamFactory {
    return@InputStreamFactory getStream()
})

fun String.createResourceFromByteArray(getStream: () -> ByteArray) = createResource { ByteArrayInputStream(getStream()) }

fun File.toStreamResource(altFile: File? = null) =
        if (altFile == null || exists())
            name.createResource { FileInputStream(this) }
        else
        // Don't call toStreamResource recursively, to avoid infinite recursion if altFile doesn't exist
            altFile.run { name.createResource { FileInputStream(this) } }
